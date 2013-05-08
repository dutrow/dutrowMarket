package dutrow.sales.jms;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is used to listen to messages on a destination. You can control the
 * durability (topics only) and selector used using the properties.
 * 
 * @author jcstaff
 */
public class Subscriber implements Runnable {
	private static final Log log = LogFactory.getLog(Subscriber.class);
	protected ConnectionFactory connFactory;
	protected Destination destination;
	protected boolean stop = false;
	protected boolean stopped = false;
	protected boolean started = false;
	protected String name;
	protected int count = 0;
	protected long sleepTime = 0;
	protected int maxCount = 0;
	protected boolean durable = false;
	protected String selector = null;
	protected String username;
	protected String password;

	public Subscriber(String name) {
		this.name = name;
	}

	public void setConnFactory(ConnectionFactory connFactory) {
		this.connFactory = connFactory;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public int getCount() {
		return count;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public void clearMessages() {
		count = 0;
	}

	public void stop() {
		this.stop = true;
	}

	public boolean isStopped() {
		return stopped;
	}

	public boolean isStarted() {
		return started;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void execute() throws Exception {
		Connection connection = null;
		Session session = null;
		MessageConsumer consumer = null;
		try {
			log.info("subscriber " + name + " starting:" + "durable=" + durable
					+ ", selector=" + selector + " username: " + username
					+ " password: " + password);

			connection = username == null ? connFactory.createConnection()
					: connFactory.createConnection(username, password);
			connection.setClientID(name);
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			if (durable == false) {
				try {
					session.unsubscribe(name);
				} catch (JMSException ignored) {
				}
				consumer = session.createConsumer(destination, selector);
			} else {
				consumer = session.createDurableSubscriber((Topic) destination,
						name, selector, false);
			}
			connection.start();

			stopped = stop = false;

			started = true;
			while (!stop && (maxCount == 0 || count < maxCount)) {
				Message message = consumer.receive(3000);
				if (message != null) {
					count += 1;
					StringBuilder text = new StringBuilder();
					text.append(name + " received message #" + count
							+ ", msgId=" + message.getJMSMessageID());
					if (message instanceof TextMessage) {
						text.append(", body="
								+ ((TextMessage) message).getText());
					} else if (message instanceof MapMessage) {
						MapMessage mm = (MapMessage) message;

						Enumeration e = mm.getMapNames();
						while (e.hasMoreElements()) {
							String item = (String) e.nextElement();
							
							text.append(", " + item + "=" + mm.getObject(item).toString());
						}
					}
					log.debug(text.toString());
					Thread.yield();
				}
				if (sleepTime > 0) {
					log.debug("processing message for " + sleepTime + "msecs");
					Thread.sleep(sleepTime);
				}
			}
			log.info("subscriber " + name + " stopping");
			connection.stop();
		} finally {
			stopped = true;
			started = false;
			if (consumer != null) {
				consumer.close();
			}
			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

	public void run() {
		try {
			execute();
		} catch (Exception ex) {
			log.fatal("error running " + name, ex);
		}
	}

	public static void main(String args[]) {
		boolean noExit = false;
		try {
			String connFactoryJNDI = null;
			String destinationJNDI = null;
			String name = "";
			Long sleepTime = null;
			Integer maxCount = null;
			Boolean durable = null;
			String selector = null;
			String username = null;
			String password = null;
			for (int i = 0; i < args.length; i++) {
				if ("-jndi.name.connFactory".equals(args[i])) {
					connFactoryJNDI = args[++i];
				} else if ("-jndi.name.destination".equals(args[i])) {
					destinationJNDI = args[++i];
				} else if ("-name".equals(args[i])) {
					name = args[++i];
				} else if ("-name".equals(args[i])) {
					name = args[++i];
				} else if ("-sleep".equals(args[i])) {
					sleepTime = new Long(args[++i]);
				} else if ("-max".equals(args[i])) {
					maxCount = new Integer(args[++i]);
				} else if ("-durable".equals(args[i])) {
					durable = new Boolean(args[++i]);
				} else if ("-selector".equals(args[i])) {
					selector = args[++i];
				} else if ("-username".equals(args[i])) {
					username = args[++i];
				} else if ("-password".equals(args[i])) {
					password = args[++i];
				} else if ("-noExit".equals(args[i])) {
					noExit = true;
				}
			}
			if (connFactoryJNDI == null) {
				throw new Exception("jndi.name.connFactory not supplied");
			} else if (destinationJNDI == null) {
				throw new Exception("jndi.name.destination not supplied");
			}
			Subscriber subscriber = new Subscriber(name);
			Context jndi = new InitialContext();
			subscriber.setConnFactory((ConnectionFactory) jndi
					.lookup(connFactoryJNDI));
			subscriber.setDestination((Destination) jndi
					.lookup(destinationJNDI));
			if (maxCount != null) {
				subscriber.setMaxCount(maxCount);
			}
			if (sleepTime != null) {
				subscriber.setSleepTime(sleepTime);
			}
			if (durable != null) {
				subscriber.setDurable(durable);
			}
			if (selector != null) {
				subscriber.setSelector(selector);
			}
			subscriber.setUsername(username);
			subscriber.setPassword(password);
			subscriber.execute();
		} catch (Exception ex) {
			log.fatal("", ex);
			System.exit(-1);
			if (noExit) {
				throw new RuntimeException("error in subscriber", ex);
			}
			System.exit(-1);
		}
	}
}

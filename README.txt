Dan Dutrow
Enterprise Java
Spring 2013

Testing:
	All you will have to do is run mvn clean install from the dutrowMarket directory.
	You can run with the h2db or h2srv profiles.

Notes:
	TestSupport is itself tested through the other *IT tests.
	There was no explanation what the listMy* methods were supposed to do, so I made the "me" a userId parameter
	I couldn't think of why we would want a transaction other than REQUIRED, so
		all transactions are @TransactionAttribute(TransactionAttributeType.REQUIRED)
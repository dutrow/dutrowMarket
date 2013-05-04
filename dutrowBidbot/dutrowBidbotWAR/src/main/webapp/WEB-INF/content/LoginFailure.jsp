<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
<title>SecurePing Login Form</title>
</head>
<body>
	<h1>Login Failure</h1>

	<form action="j_security_check" method="POST">
		<table border="0" width="30%" cellspacing="3" cellpadding="2">
			<tr>
				<td><b>User Name</b></td>
				<td><input type="text" size="20" name="j_username"></td>
			</tr>
			<tr>
				<td><b>Password</b></td>
				<td><input type="password" size="10" name="j_password"></td>
			</tr>
			<tr>
				<td><p>
						<input type="submit" value="Login"></td>
			</tr>
		</table>
	</form>

	<p />
	Test accounts:
	<table border="1" class="bodyTable">
		<tbody>
			<tr class="a">
				<td align="left"><b>Login</b></td>
				<td align="left"><b>Roles</b></td>
			</tr>
			<tr class="b">
				<td align="left">known</td>
				<td align="left">(no roles)</td>
			</tr>
			<tr class="a">
				<td align="left">admin1</td>
				<td align="left">esales-admin</td>
			</tr>
			<tr class="b">
				<td align="left">admin2</td>
				<td align="left">ebidbot-admin</td>
			</tr>
			<tr class="a">
				<td align="left">syssales1</td>
				<td align="left">esales-sys</td>
			</tr>
			<tr class="b">
				<td align="left">sysbidbot1</td>
				<td align="left">esales-trusted</td>
			</tr>
			<tr class="a">
				<td align="left">user1</td>
				<td align="left">esales-user</td>
			</tr>
			<tr class="b">
				<td align="left">user2</td>
				<td align="left">esales-user</td>
			</tr>
			<tr class="a">
				<td align="left">user3</td>
				<td align="left">esales-user,ebidbot-user</td>
			</tr>
		</tbody>
	</table>
</body>
</html>

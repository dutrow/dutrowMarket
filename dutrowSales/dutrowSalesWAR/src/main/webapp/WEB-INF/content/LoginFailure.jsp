<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">

<html>
  <head><title>SecurePing Login Form</title></head>
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
             <td><p><input type="submit" value="Login"></td>
          </tr>
       </table>
    </form>

    <p/>
    Test accounts:
     <ul>
        <li>admin1/password - an admin that is only an admin</li>
        <li>admin2/password - an admin that is also a user</li>
        <li>user1/password</li>
        <li>known/password - someone who has a login, but no permissions</li>
     </ul>
  </body>
</html>

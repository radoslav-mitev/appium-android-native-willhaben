    Java Mobile Data Driven Testing for ANDROID native apps with APPIUM 

(Tools and Frameworks used - Appium, TestNG, JDBC (MySQL), Apache POI (Excel), POM (PageFactory), Maven, BitBucket and JIRA)

Test Object – the login process on the "Willhaben" app.
  
Test Features – three of more possible error messages:
      "Die angegebene E-Mail-Adresse bzw. das Passwort konnten nicht erkannt werden.",
      "E-Mail-Adresse",
      "Passwort".

Test Technique - black-box (3 functional negative login tests are performed - for more details concerning the test cases, 
see the Test_Plan_WiHa.xlsx).

Test Data - for demonstration purposes the present project involves integration of 3 data sources:
        - TestNG parameters,
        - Excel file (Apache POI) and
        - MySQL data base (JDBC).
 
Project management tool - JIRA Software Cloud.

    Installation instructions:
After forking and cloning this repository you need to copy the folder "db_login_data" from
"src/main/java/testdataAndDriver/" into "MySQL\mysqlx\data" of your own MySQL DB (not included here 
for space reasons).

    How to run the project?
1. Start your DBMS and your Appium Server.
2. In order to execute all test included, you have to run the TESTNG.XML.
- In Spring IDE     
- Import this maven project into SpringIDE/EclipseIDE  
 
- Deploy on GAE  
    - Modify the field `<application>your_google_app_id</application>` with your own `google app ID` in the file `src/main/webapp/WEB-INF/appengine-web.xml` 
    - Add client_secrets in src/main/resources
	- Modify the parameters in ConfigParameters.java according to your application
    - Click **Run As ->  Maven build**    
    - Fill in `appengine:update` in the **Goals** (clean install) field and click **Run**    
	-Deploy with:  appcfg.cmd -A nameOfapplication update \target\appengine-1.0.0-BUILD-SNAPSHOT
     
-References:
GMAIL API: https://developers.google.com/gmail/api/
Translate API: https://code.google.com/p/google-api-translate-java/
Add external jars to maven:http://charlie.cu.cc/2012/06/how-add-external-libraries-maven/



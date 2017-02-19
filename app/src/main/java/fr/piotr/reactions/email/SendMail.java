package fr.piotr.reactions.email;
 
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import fr.piotr.reactions.R;


//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendMail extends AsyncTask<Void,Void,Void> {
 
    //Declaring Variables
    private Context context;

    //Information to send email
    private String email;
    private String message;
 
//    //Progressdialog to show while sending email
//    private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context, String email, String message){
        //Initializing variables
        this.context = context;
        this.email = email;
        this.message = message;
    }
 
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
//        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }
 
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
//        progressDialog.dismiss();
        //Showing a success message
//        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }
 
    @Override
    protected Void doInBackground(Void... params) {
        Session session = createSession();

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);
 
            //Setting sender address
            mm.setFrom(new InternetAddress(email));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(context.getString(R.string.mail_sender_subject));
            //Adding message
            mm.setText(message);

            //Sending email
            Transport.send(mm);
 
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Session createSession() {
//        //Creating properties
//        Properties properties = new Properties();
//
//        //Configuring properties for gmail
//        //If you are not using gmail you may need to change the values
//        properties.put("mail.smtp.host", context.getString(R.string.smtphost));
//        properties.put("mail.smtp.port", context.getString(R.string.smtport));
//        properties.put("mail.smtp.auth", "false");
//        //Put below to false, if no https is needed
//        properties.put("mail.smtp.starttls.enable", "true");
//
//        return Session.getInstance(properties);

        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        return Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, "");//FIXME
                    }
                });
    }
}
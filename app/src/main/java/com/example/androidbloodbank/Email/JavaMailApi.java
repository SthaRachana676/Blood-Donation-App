package com.example.androidbloodbank.Email;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidbloodbank.R;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailApi extends AsyncTask {
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final String email;
    private final String subject;
    private final String message;
    private ProgressDialog progressDialog;

    public JavaMailApi(Context context, String email, String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        boolean bool;
        Properties props = new Properties();
       // props.put("mail.smtp.host", "newsummit-edu-np.mail.protection.outlook.com");
       // props.put("mail.smtp.host", "mail.aplustech.com.np");
        props.put("mail.smtp.host", "mail.learngaroo.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Util.EMAIL, Util.PASSWORD);
            }
        });
        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(Util.EMAIL));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subject);
            mm.setText(message);
            Transport.send(mm);
            bool = true;
        } catch (MessagingException e) {
            e.printStackTrace();
            bool = false;
        }
        return bool;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false, false);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        boolean bool = (boolean) o;
        progressDialog.dismiss();
        if (bool) {
            startAlertDialog("Email Sent Successfully");
        }else {
            {
                startAlertDialog("Something went wrong");
            }
        }
    }

    private void startAlertDialog(String outputMessage) {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(R.layout.output_layout, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        Button closeButton = myView.findViewById(R.id.closeButton);
        TextView outputText = myView.findViewById(R.id.outputText);
        outputText.setText(outputMessage);

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}
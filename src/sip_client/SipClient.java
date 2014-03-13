package sip_client;

import javax.sip.*;
import javax.swing.*;

import java.net.*;
import java.text.ParseException;
import java.util.*;

import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;

public class SipClient extends JFrame implements SipListener {
  
	// Objects used to communicate to the JAIN SIP API.
	SipFactory sipFactory;          // Used to access the SIP API.
	SipStack sipStack;              // The SIP stack.
	SipProvider sipProvider;        // Used to send SIP messages.
	MessageFactory messageFactory;  // Used to create SIP message factory.
	HeaderFactory headerFactory;    // Used to create SIP headers.
	AddressFactory addressFactory;  // Used to create SIP URIs.
	ListeningPoint listeningPoint;  // SIP listening IP address/port.
	Properties properties;          // Other properties.

	// Objects keeping local configuration.
	String ip;                      // The local IP address.
	int port = 6060;                // The local port.
	String protocol = "udp";        // The local protocol (UDP).
	int tag = (new Random()).nextInt(); // The local tag.
	Address contactAddress;         // The contact address.
	ContactHeader contactHeader;    // The contact header.
	Dialog currentDialog;			//global dialog variable so that Bye request can be sent later on
	RequestEvent currentRequestEvent;
	
	public static String username;
	public static String serverIP;
    /**
     * Creates new form SipClient
     */
    public SipClient() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        buttonAccept = new javax.swing.JButton();
        buttonRegisterStatefull = new javax.swing.JButton();
        buttonInvite = new javax.swing.JButton();
        buttonBye = new javax.swing.JButton();
        textField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIP Client");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                onOpen(evt);
            }
        });

        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setRows(5);
        scrollPane.setViewportView(textArea);

        buttonAccept.setText("Accept");
        buttonAccept.setEnabled(false);
        buttonAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // SEND 200 OK, ACCEPT CALL
            	try {
	            	ServerTransaction transaction = currentRequestEvent.getServerTransaction();
	    	        if(null == transaction) {
	    	            transaction = sipProvider.getNewServerTransaction(currentRequestEvent.getRequest());
	    	        }
    	        	Response response;
				
					response = messageFactory.createResponse(200, currentRequestEvent.getRequest());
					((ToHeader)response.getHeader("To")).setTag(String.valueOf(tag));
	    	        response.addHeader(contactHeader);
	    	        transaction.sendResponse(response);
	    	        
	    	        textArea.append("\nSent response: (Accepted) " + response.toString());
	    	    
            	}
            	catch(SipException e) {            
	    	        textArea.append("\nERROR (SIP): " + e.getMessage());
	    	    }
	    	    catch(Exception e) {
	    	        textArea.append("\nERROR: " + e.getMessage());
	    	    }
    	        
    	        //this.textArea.append(" / SENT " + response.getStatusCode() + " " + response.getReasonPhrase());
    	        
            }
        });

        buttonRegisterStatefull.setText("Reg (SF)");
        buttonRegisterStatefull.setEnabled(true);
        buttonRegisterStatefull.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRegisterStatefull(evt);
            }
        });

        buttonInvite.setText("Invite");
        buttonInvite.setEnabled(true);
        buttonInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onInvite(evt);
            }
        });

        buttonBye.setText("Bye");
        buttonBye.setEnabled(false);
        buttonBye.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onBye(evt);
            }
        });

<<<<<<< HEAD
        textField.setText("sip:simon@192.168.0.6:5060");
=======
        textField.setText("sip:kristoffer@192.168.0.6:5060");
>>>>>>> FETCH_HEAD

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonRegisterStatefull, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonInvite, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonBye, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE))
                    .addComponent(textField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonAccept)
                    .addComponent(buttonInvite)
                    .addComponent(buttonBye)
                    .addComponent(buttonRegisterStatefull))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onOpen(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onOpen
        // A method called when you open your application.
    	try {
    	    // Get the local IP address.
    	    this.ip = InetAddress.getLocalHost().getHostAddress();
    	    // Create the SIP factory and set the path name.
    	    this.sipFactory = SipFactory.getInstance();
    	    this.sipFactory.setPathName("gov.nist");
    	    // Create and set the SIP stack properties.
    	    this.properties = new Properties();
    	    this.properties.setProperty("javax.sip.STACK_NAME", "stack");
    	    // Create the SIP stack.
    	    this.sipStack = this.sipFactory.createSipStack(this.properties);
    	    // Create the SIP message factory.
    	    this.messageFactory = this.sipFactory.createMessageFactory();
    	    // Create the SIP header factory.
    	    this.headerFactory = this.sipFactory.createHeaderFactory();
    	    // Create the SIP address factory.
    	    this.addressFactory = this.sipFactory.createAddressFactory();
    	    // Create the SIP listening point and bind it to the local IP address, port and protocol.
    	    this.listeningPoint = this.sipStack.createListeningPoint(this.ip, this.port, this.protocol);
    	    // Create the SIP provider.
    	    this.sipProvider = this.sipStack.createSipProvider(this.listeningPoint);
    	    // Add our application as a SIP listener.
    	    this.sipProvider.addSipListener(this);
    	    // Create the contact address used for all SIP messages.
    	    this.contactAddress = this.addressFactory.createAddress("sip:"+ this.ip + ":" + this.port);
    	    // Create the contact header used for all SIP messages.
    	    this.contactHeader = this.headerFactory.createContactHeader(contactAddress);

    	    // Display the local IP address and port in the text area.
    	    this.textArea.append("Local address: " + this.ip + ":" + this.port + "\n");
    	}
    	catch(Exception e) {
    	    // If an error occurs, display an error message box and exit.
    	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    	    System.exit(-1);
    	}
    	
    }//GEN-LAST:event_onOpen

    private void onRegisterStateless(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRegisterStateless
        // A method called when you click on the "Reg (SL)" button.
    	try {
    	    // Get the destination address from the text field.
    	    Address addressTo = this.addressFactory.createAddress(this.textField.getText());
    	    // Create the request URI for the SIP message.
    	    javax.sip.address.URI requestURI = addressTo.getURI();
    	    
    	    // Create the SIP message headers.

    	    // The "Via" headers.
    	    ArrayList viaHeaders = new ArrayList();
    	    ViaHeader viaHeader = this.headerFactory.createViaHeader(this.ip, this.port, "udp", null);
    	    viaHeaders.add(viaHeader);
    	    // The "Max-Forwards" header.
    	    MaxForwardsHeader maxForwardsHeader = this.headerFactory.createMaxForwardsHeader(70);
    	    // The "Call-Id" header.
    	    CallIdHeader callIdHeader = this.sipProvider.getNewCallId();
    	    // The "CSeq" header.
    	    CSeqHeader cSeqHeader = this.headerFactory.createCSeqHeader(1L,"REGISTER");
    	    // The "From" header.
    	    FromHeader fromHeader = this.headerFactory.createFromHeader(this.contactAddress, String.valueOf(this.tag));
    	    // The "To" header.
    	    ToHeader toHeader = this.headerFactory.createToHeader(addressTo, null);

    	    // Create the REGISTER request.
    	    Request request = this.messageFactory.createRequest(
    	        requestURI,
    	        "REGISTER",
    	        callIdHeader,
    	        cSeqHeader,
    	        fromHeader,
    	        toHeader,
    	        viaHeaders,
    	        maxForwardsHeader);
    	    // Add the "Contact" header to the request.
    	    request.addHeader(contactHeader);

    	    // Send the request statelessly through the SIP provider.
    	    this.sipProvider.sendRequest(request);

    	    // Display the message in the text area.
    	    this.textArea.append(
    	        "Request sent:\n" + request.toString() + "\n\n");
    	}
    	catch(Exception e) {
    	    // If an error occurred, display the error.
    	    this.textArea.append("Request sent failed: " + e.getMessage() + "\n");
    	}
    }//GEN-LAST:event_onRegisterStateless
    
    private void onRegisterStatefull(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRegisterStatefull
        // A method called when you click on the "Reg (SF)" button.
    	try {
    	    // Get the destination address from the text field.
    	    Address addressTo = this.addressFactory.createAddress(this.textField.getText());
    	    // Create the request URI for the SIP message.
    	    javax.sip.address.URI requestURI = addressTo.getURI();
    	    
    	    String useraddress = this.textField.getText();
    	    String[] list = useraddress.split("@");
    	    username = list[0].substring(4);
    	    serverIP = list[1];
    	    // Create the SIP message headers.

    	    // The "Via" headers.
    	    ArrayList viaHeaders = new ArrayList();
    	    ViaHeader viaHeader = this.headerFactory.createViaHeader(this.ip, this.port, "udp", null);
    	    viaHeaders.add(viaHeader);
    	    // The "Max-Forwards" header.
    	    MaxForwardsHeader maxForwardsHeader = this.headerFactory.createMaxForwardsHeader(70);
    	    // The "Call-Id" header.
    	    CallIdHeader callIdHeader = this.sipProvider.getNewCallId();
    	    // The "CSeq" header.
    	    CSeqHeader cSeqHeader = this.headerFactory.createCSeqHeader(1L,"REGISTER");
    	    // The "From" header.
    	    FromHeader fromHeader = this.headerFactory.createFromHeader(this.contactAddress, String.valueOf(this.tag));
    	    // The "To" header.
    	    ToHeader toHeader = this.headerFactory.createToHeader(addressTo, null);
    	    this.contactAddress = this.addressFactory.createAddress("sip:"+username+"@"+this.ip + ":" + this.port);
    	    this.contactHeader = this.headerFactory.createContactHeader(contactAddress);
    	    // Create the REGISTER request.
    	    Request request = this.messageFactory.createRequest(
    	        requestURI,
    	        "REGISTER",
    	        callIdHeader,
    	        cSeqHeader,
    	        fromHeader,
    	        toHeader,
    	        viaHeaders,
    	        maxForwardsHeader);
    	    // Add the "Contact" header to the request.
    	    request.addHeader(contactHeader);

    	    ClientTransaction transaction = this.sipProvider.getNewClientTransaction(request);
    	    // Send the request statefully, through the client transaction.
    	    transaction.sendRequest();

    	    // Display the message in the text area.
    	    this.textArea.append(
    	        "Request sent:\n" + request.toString() + "\n\n");
    	}
    	catch(Exception e) {
    	    // If an error occurred, display the error.
    	    this.textArea.append("Request sent failed: " + e.getMessage() + "\n");
    	}
    }//GEN-LAST:event_onRegisterStatefull

    private void onInvite(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onInvite
        // A method called when you click on the "Invite" button.
    	//create a new INVITE request and send it to the server, with the intended SIP recipient
    	try {
    	    // Get the server address from the text field.
    	    Address addressTo = this.addressFactory.createAddress(this.textField.getText());
    	    Address addressFrom = this.addressFactory.createAddress("sip:"+username+"@"+serverIP);
    	    // Create the request URI for the SIP message.
    	    javax.sip.address.URI requestURI = addressTo.getURI();

    	    // Create the SIP message headers.

    	    // The "Via" headers.
    	    ArrayList viaHeaders = new ArrayList();
    	    ViaHeader viaHeader = this.headerFactory.createViaHeader(this.ip, this.port, "udp", null);
    	    viaHeaders.add(viaHeader);
    	    // The "Max-Forwards" header.
    	    MaxForwardsHeader maxForwardsHeader = this.headerFactory.createMaxForwardsHeader(70);
    	    // The "Call-Id" header.
    	    CallIdHeader callIdHeader = this.sipProvider.getNewCallId();
    	    // The "CSeq" header.
    	    CSeqHeader cSeqHeader = this.headerFactory.createCSeqHeader(1L,"INVITE");	//maybe change the 1L?
    	    // The "From" header.
    	    System.out.println("CONTACT ADDRESS"+this.contactAddress);
    	    FromHeader fromHeader = this.headerFactory.createFromHeader(addressFrom, String.valueOf(this.tag));
    	    // The "To" header.
    	    ToHeader toHeader = this.headerFactory.createToHeader(addressTo, null);
    	    // Content-type 
    	    ContentTypeHeader contentTypeHeader = this.headerFactory.createContentTypeHeader("application", "sdp");

    	    // Create the INVITE request.
    	    Request request = this.messageFactory.createRequest(
    	        requestURI,
    	        "INVITE",
    	        callIdHeader,
    	        cSeqHeader,
    	        fromHeader,
    	        toHeader,
    	        viaHeaders,
    	        maxForwardsHeader);
    	    // Add the "Contact" header to the request.
    	    request.addHeader(contactHeader);
    	    request.addHeader(contentTypeHeader);

    	    ClientTransaction transaction = this.sipProvider.getNewClientTransaction(request);
    	    // Send the request statefully, through the client transaction.
    	    transaction.sendRequest();

    	    // Display the message in the text area.
    	    this.textArea.append(
    	        "Request sent:\n" + request.toString() + "\n\n");
    	}
    	catch(Exception e) {
    	    // If an error occurred, display the error.
    	    this.textArea.append("Request sent failed: " + e.getMessage() + "\n");
    	}
    }//GEN-LAST:event_onInvite

    private void onBye(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onBye
        // A method called when you click on the "Bye" button.
    }//GEN-LAST:event_onBye

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SipClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SipClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SipClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SipClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SipClient().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonBye;
    private javax.swing.JButton buttonInvite;
    private javax.swing.JButton buttonRegisterStatefull;
    private javax.swing.JButton buttonAccept;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    private javax.swing.JTextField textField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void processRequest(RequestEvent requestEvent) {
        // A method called when you receive a SIP request.
    	// when a invite is received, send 180 trying or whatever
    	//A ringtone should commence and if the user picks up, the ringing stops and 200 ok is sent or whatever
    	//Maybe start a timer and if the user doesnt pick up or reject incoming call, send 603 DECLINE or whatever
    	 this.textArea.append("\nReceived request: " + requestEvent.getRequest().toString());
    	
    	try {
	    	Request request = requestEvent.getRequest();
	    	currentRequestEvent = requestEvent;
	    	System.out.println(request.toString());
	    	
	    	ServerTransaction transaction = requestEvent.getServerTransaction();
	        if(null == transaction) {
	            transaction = this.sipProvider.getNewServerTransaction(request);
	        }
	    	//send 180 Ringing back to UA
	        //wrap this in a IF request = INVITE etc.
	        if(request.getMethod().equals("INVITE")){
	        	Response response = this.messageFactory.createResponse(180, request);
		        ((ToHeader)response.getHeader("To")).setTag(String.valueOf(this.tag));
		        response.addHeader(this.contactHeader);
		        transaction.sendResponse(response);
		        
		        //this.textArea.append(" / SENT " + response.getStatusCode() + " " + response.getReasonPhrase());
		        this.textArea.append("\nSent response: " + response.toString());
		        //MAKE AN ANSWER BUTTON BLINK AND CLICKABLE etc.
		        buttonAccept.setEnabled(true);
	        }
	        else if(request.getMethod().equals("ACK")) {
	        	this.textArea.append("\nReceived final ACK: ");
	        }
	        
    	
    	}
        catch(SipException e) {            
            this.textArea.append("\nERROR (SIP): " + e.getMessage());
        }
        catch(Exception e) {
            this.textArea.append("\nERROR: " + e.getMessage());
        }
    
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        // A method called when you receive a SIP response.
    	// Get the response.
    	Response response = responseEvent.getResponse();
    	// Display the response message in the text area.
    	//System.out.println(response.getStatusCode());
    	this.textArea.append("\nReceived response: " + response.toString());
    	
    	if (response.getStatusCode() == 200 && response.getHeader("CSeq").toString().contains("REGISTER")) {
    		//System.out.println(response.getHeader("CSeq").toString());
    	}
    	else if (response.getStatusCode() == 180 && response.getHeader("CSeq").toString().contains("RINGING")) {
    		//sysout the 180 message
    	}
    	//what to do when a 200 OK on invite is received  -> send ACK
    	else if (response.getStatusCode() == 200 && response.getHeader("CSeq").toString().contains("INVITE")) {
    		//System.out.println(response.getHeader("CSeq").toString());
    		currentDialog = responseEvent.getClientTransaction().getDialog();
    		if (currentDialog != null) {
    			try {	//create an ACK request and send it
					Request request = currentDialog.createAck(((CSeqHeader)response.getHeader("CSeq")).getSeqNumber());
					currentDialog.sendAck(request);
					this.textArea.append("\nSuccessfully established a DIALOG!");
    			} catch (InvalidArgumentException e) {
					e.printStackTrace();
				} catch (SipException e) {
					e.printStackTrace();
				}
    		}
    	}
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        // A method called when a SIP operation times out.
    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {
        // A method called when a SIP operation results in an I/O error.
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        // A method called when a SIP transaction terminates.
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        // A method called when a SIP dialog terminates.
    }
}

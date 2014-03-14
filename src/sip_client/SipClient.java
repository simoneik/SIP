package sip_client;

import javax.sip.*;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.*;
import java.util.*;

import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;

public class SipClient extends JFrame implements SipListener {
  
	// JAIN SIP API VARIABLESg
	SipFactory sipFactory;          	// ACCESS SIP API
	SipStack sipStack;              	// SIP STACK
	SipProvider sipProvider;        	// SEND SIP MESSAGES.
	MessageFactory messageFactory;  	// CREATE SIP MESSAGE FACTORY
	HeaderFactory headerFactory;    	// CREATE SIP HEADERS
	AddressFactory addressFactory;  	// CREATE SIP URIS
	ListeningPoint listeningPoint;  	// SIP LISTENING IP ADDRESS/PORT
	Properties properties;          	// OTHER PROPERTIES

	// LOCAL CONFIGURATION
	String ip;                     		// IP ADDRESS
	int port = 6060;                	// PORT
	String protocol = "udp";        	// UDP - PROTOCOL
	int tag = (new Random()).nextInt(); // TAG.
	Address contactAddress;         	// CONTACT ADDRESS
	ContactHeader contactHeader;    	// CONTACT HEADER
	Dialog currentDialog;				// DIALOG
	RequestEvent myRequestEvent;		// REQUEST EVENT
	ServerTransaction myTransaction;	// TRANSACTION
	
	public static String username;		// USERNAME
	public static String serverIP;		// SERVER IP
	
	private JButton buttonBye;			// BYE BUTTON
    private JButton buttonInvite;		// INVITE BUTTON
    private JButton buttonRegister;		// REGISTER BUTTON
    private JButton buttonAccept;		// ACCEPT BUTTON
    private JScrollPane scrollPane;		// SCROLL PANE
    private JTextArea textArea;			// TEXTAREA
    private JTextField textField;		// REGISTER TEXTFIELD
    private JTextField textField2;		// INVITE TEXTFIELD
    private JFrame frame;				// FRAME

    public static void main(String args[]) {
   	 	new SipClient();
    }
    
    public SipClient() {
    	 // CREATE AND SET UP WINDOW
        frame = new JFrame("SIP Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // ADD ALL NECESARRY GUI
        initGUI(frame.getContentPane());
        // DISPLAY THE WINDOW
        frame.pack();
        frame.setVisible(true);
        // INITIALIZE VARIABLES
        initVariables();
    }
     
    private void initGUI(Container panel) {
 
    	// SCROLLPANE
        scrollPane = new JScrollPane();
        
        // TEXTAREA
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setRows(20);
        scrollPane.setViewportView(textArea);
        
        // ACCEPT
        buttonAccept = new JButton("ACCEPT");
        buttonAccept.setEnabled(false);
        buttonAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {            	
    	        processRequest(myRequestEvent);
            }
        });
        
        // REGISTER
        buttonRegister = new JButton("REGISTER");
        buttonRegister.setEnabled(true);
        buttonRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRegister(evt);
            }
        });

        // INVITE
        buttonInvite = new JButton("INVITE");
        buttonInvite.setEnabled(true);
        buttonInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onInvite(evt);
            }
        });
        
        // BYE
        buttonBye = new JButton("BYE");
        buttonBye.setEnabled(false);
        buttonBye.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onBye(evt);
                buttonBye.setEnabled(false);
                buttonAccept.setEnabled(false);
                textArea.append("****************************************\nYOU HUNG UP\n****************************************\n");
            }
        });
        
        // TEXTFIELD
        textField = new JTextField("sip:simon@192.168.0.6:5060");
        textField2 = new JTextField("sip:kristoffer@192.168.0.6:5060");
        
        JPanel topPanel = new JPanel();
        GridLayout topLayout = new GridLayout(2,1);
        topPanel.setLayout(topLayout);
        topPanel.add(textField);
        topPanel.add(buttonRegister);
        topPanel.add(textField2);
        topPanel.add(buttonInvite);
        
        JPanel bottomPanel = new JPanel();
        GridLayout bottomLayout = new GridLayout(1,0);
        bottomPanel.setLayout(bottomLayout);
        bottomPanel.add(buttonAccept);
        bottomPanel.add(buttonBye);
        
        panel.add(topPanel, BorderLayout.PAGE_START);	
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.PAGE_END);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
            
    }
    private void initVariables() {
    	try {
    	    this.ip = InetAddress.getLocalHost().getHostAddress();												// GET LOCAL ADDRESS
    	    this.sipFactory = SipFactory.getInstance();															// SIP FACTORY
    	    this.sipFactory.setPathName("gov.nist");															// PATH NAME
    	    this.properties = new Properties();																	// SIP STACK PROPERTIES
    	    this.properties.setProperty("javax.sip.STACK_NAME", "stack");
    	    this.sipStack = this.sipFactory.createSipStack(this.properties);									// SIP STACK
    	    this.messageFactory = this.sipFactory.createMessageFactory();										// SIP MESSAGE FACTORY
    	    this.headerFactory = this.sipFactory.createHeaderFactory();											// SIP HEADER FACTORY
    	    this.addressFactory = this.sipFactory.createAddressFactory();										// SIP ADDRESS FACTORY
    	    this.listeningPoint = this.sipStack.createListeningPoint(this.ip, this.port, this.protocol);		// SIP LISTENING POIN, BIND IT TO LOCAL IP ADDRESS, PORT AND PROTOCOL
    	    this.sipProvider = this.sipStack.createSipProvider(this.listeningPoint);							// SIP PROVIDER
    	    this.sipProvider.addSipListener(this);																// ADD SIP LISTENER
    	    this.contactAddress = this.addressFactory.createAddress("sip:"+ this.ip + ":" + this.port);    	    // CONTACT ADDRESS FOR ALL SIP MESSAGES
    	    this.contactHeader = this.headerFactory.createContactHeader(contactAddress);						// CONTACT HEADER FOR ALL SIP MESSAGES

    	    // LOCAL IP ADDRESS IS DISPLAYED
    	    this.textArea.append("****************************************\nLOCAL ADDRESS\n****************************************\n" + this.ip + ":" + this.port + "\n\n");
    	}
    	catch(Exception e) {
    	    // ERROR MESSAGE
    	    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    	    System.exit(-1);
    	}
    }
    
    private void onRegister(ActionEvent evt) {
    	try {
    	    // GET DESTINATION ADDRESS FROM TEXTFIELD
    	    Address addressTo = this.addressFactory.createAddress(this.textField.getText());			
    	    // REQUEST URI FOR THE SIP MESSAGE
    	    javax.sip.address.URI requestURI = addressTo.getURI();
    	    String useraddress = this.textField.getText();
    	    String[] list = useraddress.split("@");
    	    username = list[0].substring(4);
    	    serverIP = list[1];
    	    
    	    // SIP MESSAGE HEADERS:
    	    ArrayList viaHeaders = new ArrayList();
    	    ViaHeader viaHeader = this.headerFactory.createViaHeader(this.ip, this.port, "udp", null);
    	    viaHeaders.add(viaHeader);
    	    MaxForwardsHeader maxForwardsHeader = this.headerFactory.createMaxForwardsHeader(70);
    	    CallIdHeader callIdHeader = this.sipProvider.getNewCallId();
    	    CSeqHeader cSeqHeader = this.headerFactory.createCSeqHeader(1L,"REGISTER");
    	    FromHeader fromHeader = this.headerFactory.createFromHeader(this.contactAddress, String.valueOf(this.tag));
    	    ToHeader toHeader = this.headerFactory.createToHeader(addressTo, null);
    	    this.contactAddress = this.addressFactory.createAddress("sip:"+username+"@"+this.ip + ":" + this.port);
    	    this.contactHeader = this.headerFactory.createContactHeader(contactAddress);
    	    
    	    // CREATE REGISTER REQUEST
    	    Request request = this.messageFactory.createRequest(
    	        requestURI,
    	        "REGISTER",
    	        callIdHeader,
    	        cSeqHeader,
    	        fromHeader,
    	        toHeader,
    	        viaHeaders,
    	        maxForwardsHeader);
    	    
    	    // ADD CONTACT HEADER
    	    request.addHeader(contactHeader);

    	    // SEND THE REQUEST STATEFULLY THROUGH THE CLIENTTRANSACTION
    	    ClientTransaction transaction = this.sipProvider.getNewClientTransaction(request);
    	    transaction.sendRequest();

    	    // DISPLAY THE REQUEST IN TEXTFIELD
    	    this.textArea.append("****************************************\nREGISTRATION SENT\n****************************************\n" + request.toString() + "\n");
    	}
    	catch(Exception e) {
    	    this.textArea.append("Request sent failed: " + e.getMessage() + "\n");
    	}
    }

    private void onInvite(java.awt.event.ActionEvent evt) {
    	try {
    	    // Get the server address from the text field.
    	    Address addressTo = this.addressFactory.createAddress(this.textField2.getText());
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
    	    this.textArea.append("****************************************\nINVITE SENT\n****************************************\n" + request.toString() + "\n");
    	}
    	catch(Exception e) {
    	    // If an error occurred, display the error.
    	    this.textArea.append("Request sent failed: " + e.getMessage() + "\n");
    	}
    }

    private void onBye(ActionEvent evt) {
        // A method called when you click on the "Bye" button.
    	try {
            Request byeRequest = this.currentDialog.createRequest(Request.BYE);
            ClientTransaction ct = sipProvider.getNewClientTransaction(byeRequest);
            this.currentDialog.sendRequest(ct);
         } catch (Exception ex) {
             ex.printStackTrace();
             System.exit(0);
         }
    }

    @Override
    public void processRequest(RequestEvent requestEvent) {
    	try {
	    	Request request = requestEvent.getRequest();
	        if(request.getMethod().equals("INVITE") && !this.buttonAccept.isEnabled()){
	        	 this.textArea.append("****************************************\nINVITE RECEIVED\n****************************************\n" + request.toString() + "\n");
	        	ServerTransaction transaction = requestEvent.getServerTransaction();
		        if(null == transaction) {
		            transaction = this.sipProvider.getNewServerTransaction(request);
		        }
		        
	        	Response response = this.messageFactory.createResponse(180, request);
		        ((ToHeader)response.getHeader("To")).setTag(String.valueOf(this.tag));
		        response.addHeader(this.contactHeader);
		        transaction.sendResponse(response);	        
		        storeTransaction(transaction);
		        storeRequest(requestEvent);
		        this.textArea.append("****************************************\nRESPONSE SENT - 180 RINGING\n****************************************\n" + response.toString() + "\n");
		        buttonAccept.setEnabled(true);
	        }
	        else if(request.getMethod().equals("INVITE") && this.buttonAccept.isEnabled()){
	        	Response response2 = this.messageFactory.createResponse(200, getMyRequest());
		        ((ToHeader)response2.getHeader("To")).setTag(String.valueOf(this.tag));
		        response2.addHeader(this.contactHeader);
		        getTransaction().sendResponse(response2);
		        this.textArea.append("****************************************\nACCEPTED, RESPONSE SENT - 200 OK\n****************************************\n" + response2.toString() + "\n");
	        }
	        else if(request.getMethod().equals("ACK")) {
	        	buttonBye.setEnabled(true);
	        	this.textArea.append("****************************************\nACK RECEIVED\n****************************************\n"+request.toString()+"\n\n");
	        	currentDialog = requestEvent.getServerTransaction().getDialog();
	        }
	        else if(request.getMethod().equals("BYE")){
	        	ServerTransaction transaction = requestEvent.getServerTransaction();
		        if(null == transaction) {
		            transaction = this.sipProvider.getNewServerTransaction(request);
		        }
		        
	        	Response response3 = this.messageFactory.createResponse(200, request);
		        ((ToHeader)response3.getHeader("To")).setTag(String.valueOf(this.tag));
		        response3.addHeader(this.contactHeader);
		        transaction.sendResponse(response3);
		        this.textArea.append("****************************************\nYOUR CONVERSATION PARTNER HUNG UP\n****************************************\n");
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
    	Response response = responseEvent.getResponse();
    	
    	if (response.getStatusCode() == 200 && response.getHeader("CSeq").toString().contains("REGISTER")) {
    		this.textArea.append("****************************************\nRESPONSE RECEIVED - 200 OK\n****************************************\n" + response.toString() + "\n");
    	}
    	else if (response.getStatusCode() == 180) {
    		this.textArea.append("****************************************\nRESPONSE RECEIVED - 180 RINGING\n****************************************\n" + response.toString() + "\n");    
    	}
    	else if(response.getStatusCode() == 100){
    		 this.textArea.append("****************************************\nRESPONSE RECEIVED - 100 TRYING\n****************************************\n" + response.toString() + "\n");
    	}
    	else if (response.getStatusCode() == 200 && response.getHeader("CSeq").toString().contains("INVITE")) {
    		currentDialog = responseEvent.getClientTransaction().getDialog();
    		if (currentDialog != null) {
    			try {	
					Request request = currentDialog.createAck(((CSeqHeader)response.getHeader("CSeq")).getSeqNumber());
					currentDialog.sendAck(request);
					this.textArea.append("****************************************\nRECEIVED RESPONSE - 200 OK - DIALOG ESTABLISHED!\n****************************************\n"+response.toString()+"\n\n");
					this.textArea.append("****************************************\nACK SENT\n****************************************\n"+request.toString()+"\n");
					buttonBye.setEnabled(true);
    			} catch (InvalidArgumentException e) {
					e.printStackTrace();
				} catch (SipException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
	
	private void storeRequest(RequestEvent request) {
		this.myRequestEvent = request;
	}
    
    private Request getMyRequest() {
		return this.myRequestEvent.getRequest();
	}

	private void storeTransaction(ServerTransaction transaction) {
    	this.myTransaction = transaction;
	}

    private ServerTransaction getTransaction(){
    	return this.myTransaction;
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        // SIP TIME OUT
    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {
        // SIP I/O ERROR
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        // SIP TRANSACTION TERMINATES
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        // SIP DIALOG TERMINATES
    }
}

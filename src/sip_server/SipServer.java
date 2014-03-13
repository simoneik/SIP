/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip_server;

import java.net.*;
import java.util.*;

import javax.sip.*;
import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;
import javax.swing.*;
import javax.swing.table.*;

public class SipServer extends javax.swing.JFrame implements SipListener {
        
    private SipFactory sipFactory;
    private SipStack sipStack;
    private SipProvider sipProvider;
    private MessageFactory messageFactory;
    private HeaderFactory headerFactory;
    private AddressFactory addressFactory;
    private ListeningPoint listeningPoint;
    private Properties properties;

    private String ip;
    private int port = 5060;
    private String protocol = "udp";
    private int tag = (new Random()).nextInt();
    private Address contactAddress;
    private ContactHeader contactHeader;
    private static HashMap<String, String> users = new HashMap<String, String>(); //contains mapping of username to ip-address
    
    /**
     * Creates new form SipRegistrar
     */
    public SipServer() {
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

        jScrollPaneTable = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jScrollPaneText = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIP Server");
        setLocationByPlatform(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                onOpen(evt);
            }
        });

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Time", "URI", "From", "To", "Call-ID", "CSeq", "Dialog", "Transaction", "Type", "Request/Response"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPaneTable.setViewportView(jTable);

        jTextArea.setEditable(false);
        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jScrollPaneText.setViewportView(jTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneTable, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addComponent(jScrollPaneText)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPaneTable, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneText, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onOpen(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onOpen
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();

            this.sipFactory = SipFactory.getInstance();
            this.sipFactory.setPathName("gov.nist");
            this.properties = new Properties();
            this.properties.setProperty("javax.sip.STACK_NAME", "stack");
            this.sipStack = this.sipFactory.createSipStack(this.properties);
            this.messageFactory = this.sipFactory.createMessageFactory();
            this.headerFactory = this.sipFactory.createHeaderFactory();
            this.addressFactory = this.sipFactory.createAddressFactory();
            this.listeningPoint = this.sipStack.createListeningPoint(this.ip, this.port, this.protocol);
            this.sipProvider = this.sipStack.createSipProvider(this.listeningPoint);
            this.sipProvider.addSipListener(this);

            this.contactAddress = this.addressFactory.createAddress("sip:" + this.ip + ":" + this.port);
            this.contactHeader = this.headerFactory.createContactHeader(contactAddress);
            
            this.jTextArea.append("Local address: " + this.ip + ":" + this.port + "\n");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
        }
    }//GEN-LAST:event_onOpen

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
            java.util.logging.Logger.getLogger(SipServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SipServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SipServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SipServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SipServer().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPaneTable;
    private javax.swing.JScrollPane jScrollPaneText;
    private javax.swing.JTable jTable;
    private javax.swing.JTextArea jTextArea;
    // End of variables declaration//GEN-END:variables

    @Override
    public void processRequest(RequestEvent requestEvent) {
        // Get the request.
        Request request = requestEvent.getRequest();
        this.jTextArea.append("\n Process Request.... " + request.toString());

        this.jTextArea.append("\nRECV " + request.getMethod() + " " + request.getRequestURI().toString());        
        
        try {
            // Get or create the server transaction.
            ServerTransaction transaction = requestEvent.getServerTransaction();
            if(null == transaction) {
                transaction = this.sipProvider.getNewServerTransaction(request);
            }
            
            // Update the SIP message table.
            this.updateTable(requestEvent, request, transaction);

            // Process the request and send a response.
            Response response;
            if(request.getMethod().equals("REGISTER")) {
                // If the request is a REGISTER.
            	 //save SIP user agent to HashMap Table, together with his ip
                FromHeader from = (FromHeader)request.getHeader("From");
            	String fromAddr = from.getAddress().toString();
            	String[] list1 = request.getRequestURI().toString().split(":");
            	String[] list2 = list1[1].split("@");
            	String userName = list2[0];
            	String[] preIPAddress = from.getAddress().toString().split(":");
            	String IPAddress = preIPAddress[1]+":"+preIPAddress[2];
            	IPAddress = IPAddress.replace(">", "");
                this.jTextArea.append(" / MONGO " + IPAddress);
                users.put(userName, IPAddress);
                //iterate through HashMap
                Set userSet = users.entrySet();
    			Iterator iterator = userSet.iterator();	
    			while(iterator.hasNext()) {
    		         Map.Entry me = (Map.Entry)iterator.next();
    		         this.jTextArea.append(me.getKey()+":");
    		         this.jTextArea.append((String) me.getValue());
    			}
                //save from (without the toString()) to HashMap together with username of sender
                //send 200 OK back to UA
            	response = this.messageFactory.createResponse(200, request);
                ((ToHeader)response.getHeader("To")).setTag(String.valueOf(this.tag));
                response.addHeader(this.contactHeader);
                transaction.sendResponse(response);
                this.jTextArea.append(" / SENT " + response.getStatusCode() + " " + response.getReasonPhrase());
                //System.out.println(request.getHeaders("CSeq"));
                
               

            }
            else if(request.getMethod().equals("INVITE")) {  // If the request is an INVITE.
            	//sends 100 trying back to UA-A:
                response = this.messageFactory.createResponse(100, request);//100 trying
                ((ToHeader)response.getHeader("To")).setTag(String.valueOf(this.tag));
                response.addHeader(this.contactHeader);
                transaction.sendResponse(response);
                this.jTextArea.append(" / SENT " + response.getStatusCode() + " " + response.getReasonPhrase());
            
                //create code so that the server sends the invite to the recipient
                //find match recipients sip name to his IP address from a HashMap

                //Replace to header with ip of UA-B
        	    // Send the request statefully, through the client transaction.
                try {
            	    // Get the destination address from the text field.
                	String[] list1 = request.getRequestURI().toString().split(":");
                	String[] list2 = list1[1].split("@");
                	String userName = list2[0];
                	String userIP = users.get(userName);
                	System.out.println("sip:"+userIP);
                	if (userIP == null) {
                		System.out.println("Could not find UA-B's name in HashMap");
                		//could not find user, maybe send a 603 error message back?
                	}
            	    Address addressTo = this.addressFactory.createAddress("sip:"+userIP);
            	    // Create the request URI for the SIP message.
            	    javax.sip.address.URI requestURI = addressTo.getURI();

            	    // Create the SIP message headers.

            	    // The "Via" headers.Get existing list from incoming request
            	    ViaHeader oldViaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
            	    ArrayList viaHeaders = new ArrayList();
            	    //viaHeaders.add(oldViaHeader);
            	    ViaHeader viaHeader = this.headerFactory.createViaHeader(this.ip, this.port, "udp", null);
            	    viaHeaders.add(viaHeader);
            	    
            	    // The "Max-Forwards" header.
            	    MaxForwardsHeader maxForwardsHeader = (MaxForwardsHeader) request.getHeader(MaxForwardsHeader.NAME);//this.headerFactory.createMaxForwardsHeader(70); //get already set up max Forw
            	    // The "Call-Id" header.
            	    CallIdHeader callIdHeader = (CallIdHeader)request.getHeader("Call-Id");//get already set up call ID
            	    // The "CSeq" header.
            	    CSeqHeader cSeqHeader = (CSeqHeader)request.getHeader("CSeq"); //get already set ip CSeq (INVITE)
            	    // The "From" header.
            	    FromHeader oldFrom = (FromHeader)request.getHeader("From");
            	    String transactionTag = oldFrom.getTag();
            	    FromHeader fromHeader = this.headerFactory.createFromHeader(this.contactAddress, transactionTag);	//keep same tag
            	    // The "To" header.
            	    ToHeader toHeader = this.headerFactory.createToHeader(addressTo, null); //set toHeader to be UA-B

            	    // Create the REGISTER request.
            	    Request requestForward = this.messageFactory.createRequest(
            	        requestURI,
            	        "INVITE",
            	        callIdHeader,
            	        cSeqHeader,
            	        fromHeader,
            	        toHeader,
            	        viaHeaders,
            	        maxForwardsHeader);
            	    // Add the "Contact" header to the request.
            	    requestForward.addHeader(contactHeader);
        	    ClientTransaction clientTrans = this.sipProvider.getNewClientTransaction(requestForward);
                clientTrans.sendRequest();
                this.jTextArea.append(" / Forwarded INVITE " + requestForward.toString());
                }
                catch(Exception e) {
                	System.out.println(e);
                	e.printStackTrace();
                }
            }
            else if(request.getMethod().equals("ACK")) {
                // If the request is an ACK.
            }
            else if(request.getMethod().equals("BYE")) {
                // If the request is a BYE.
                response = this.messageFactory.createResponse(200, request);
                ((ToHeader)response.getHeader("To")).setTag(String.valueOf(this.tag));
                response.addHeader(this.contactHeader);
                transaction.sendResponse(response);
                this.jTextArea.append(" / SENT " + response.getStatusCode() + " " + response.getReasonPhrase());
            }
        }
        catch(SipException e) {            
            this.jTextArea.append("\nERROR (SIP): " + e.getMessage());
        }
        catch(Exception e) {
            this.jTextArea.append("\nERROR: " + e.getMessage());
        }
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
        //throw new UnsupportedOperationException("Not supported yet.");
    	
    	Response response = responseEvent.getResponse();
    	this.jTextArea.append("\n ProcessResponse.... " + response.getStatusCode() + " " + response.getReasonPhrase());
    	ClientTransaction transaction = responseEvent.getClientTransaction();
    	try {
    		if(response.getStatusCode()==180) {	//if 180 ringing is sent from UA-B to server
    			//FIX SO THAT THE RESPONSE IS FORWARDED TO UA-A!
                ((ToHeader)response.getHeader("To")).setTag(String.valueOf(this.tag)); //DONT use this.tag, extract the one from the response received
                response.addHeader(this.contactHeader);  //what does this do, VIA-header??
                //((SipProvider) transaction).sendResponse(response);	//forward response to UA-A
                this.sipProvider.sendResponse(response);
                this.jTextArea.append("\n / PROCESSED 180 RINGING, Forwarded it! " + response.getStatusCode() + " " + response.getReasonPhrase());
    		}
    		else if(response.getStatusCode()==200) {	//if 200 ringing is sent from UA-B to server
    			//FIX SO THAT THE RESPONSE IS FORWARDED TO UA-A!
                ((ToHeader)response.getHeader("To")).setTag(String.valueOf(this.tag)); //DONT use this.tag, extract the one from the response received
                response.addHeader(this.contactHeader);  //what does this do, VIA-header??
                ((SipProvider) transaction).sendResponse(response);	//forward response to UA-A
                this.jTextArea.append("\n / PROCESSED 200 RINGING, Forwarded it! " + response.getStatusCode() + " " + response.getReasonPhrase());
    		}
    	}
    	catch(Exception e) {
    		System.out.println(e);
    	}
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void processIOException(IOExceptionEvent exceptionEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
 
    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void updateTable(RequestEvent requestEvent, Request request, ServerTransaction transaction) {
        // Get the table model.
        DefaultTableModel tableModel = (DefaultTableModel) this.jTable.getModel();
        // Get the headers.
        FromHeader from = (FromHeader)request.getHeader("From");
        ToHeader to = (ToHeader)request.getHeader("To");
        CallIdHeader callId = (CallIdHeader)request.getHeader("Call-Id");
        CSeqHeader cSeq = (CSeqHeader)request.getHeader("CSeq");
        // Get the SIP dialog.
        Dialog dialog = transaction.getDialog();
        // Add a new line to the table.
        tableModel.addRow(new Object[] {
            (new Date()).toString(),
            request.getRequestURI() != null ? request.getRequestURI().toString() : "(unknown)",
            from != null ? from.getAddress() : "(unknown)",
            to != null ? to.getAddress() : "(unknown)",
            callId != null ? callId.getCallId() : "(unknown)",
            cSeq != null ? cSeq.getSeqNumber() + " " + cSeq.getMethod() : "(unknown)",
            dialog != null ? dialog.getDialogId() : "",
            transaction.getBranchId(),
            "Request",
            request.getMethod() });
    }
}

package com.icegreen.greenmail.spring;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fmaury
 * Date: 16/01/14
 * Time: 07:12
 * To change this template use File | Settings | File Templates.
 */
public class GreenMailBean implements InitializingBean, DisposableBean {
    /**
     * New logger.
     */
    static final Log log = LogFactory.getLog(GreenMailBean.class);

    /**
     * The mail server.
     */
    private GreenMail mGreenMail;
    /**
     * Automatically start the servers [TRUE].
     */
    private boolean mAutostart = true;
    /**
     * SMTP server
     */
    private boolean mSmtpProtocoll = true;
    /**
     * SMTPS server
     */
    private boolean mSmtpsProtocoll = false;
    /**
     * POP3 server
     */
    private boolean mPop3Protocoll = true;
    /**
     * POP3S server
     */
    private boolean mPop3sProtocoll = false;
    /**
     * IMAP server.
     */
    private boolean mImapProtocoll = false;
    /**
     * IMAPS server.
     */
    private boolean mImapsProtocoll = false;
    /**
     * Users.
     */
    private List mUsers;
    /**
     * Port offset (default is 3000)
     */
    private int mPortOffset = 3000;
    /**
     * Hostname. Default is null (= localhost).
     */
    private String mHostname;

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied (and satisfied
     * BeanFactoryAware and ApplicationContextAware). &lt;p&gt;This method allows the bean instance to
     * perform initialization only possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an essential
     *                   property) or if initialization fails.
     */
    public void afterPropertiesSet() throws Exception {
        mGreenMail = new GreenMail(createServerSetup());
        if (null != mUsers) {
            for (int i = 0; i < mUsers.size(); i++) {
                String user = (String) mUsers.get(i);
                int posColon = user.indexOf(':');
                int posAt = user.indexOf('@');
                String login = user.substring(0, posColon);
                String pwd = user.substring(posColon + 1, posAt);
                String domain = user.substring(posAt + 1);
                if (log.isDebugEnabled()) {
                    log.debug("Adding user " + login + ':' + pwd + '@' + domain);
                }
                mGreenMail.setUser(login + '@' + domain, login, pwd);
            }
        }
        if (mAutostart) {
            mGreenMail.start();
        }
    }

    /**
     * Creates the server setup, depending on the protocoll flags.
     *
     * @return the configured server setups.
     */
    private ServerSetup[] createServerSetup() {
        List setups = new ArrayList();
        if (mSmtpProtocoll) {
            setups.add(createTestServerSetup(ServerSetup.SMTP));
        }
        if (mSmtpsProtocoll) {
            setups.add(createTestServerSetup(ServerSetup.SMTPS));
        }
        if (mPop3Protocoll) {
            setups.add(createTestServerSetup(ServerSetup.POP3));
        }
        if (mPop3sProtocoll) {
            setups.add(createTestServerSetup(ServerSetup.POP3S));
        }
        if (mImapProtocoll) {
            setups.add(createTestServerSetup(ServerSetup.IMAP));
        }
        if (mImapsProtocoll) {
            setups.add(createTestServerSetup(ServerSetup.IMAPS));
        }
        return (ServerSetup[]) setups.toArray(new ServerSetup[0]);
    }

    /**
     * Creates a test server setup with configured offset.
     *
     * @param theSetup the server setup.
     * @return the test server setup.
     */
    private ServerSetup createTestServerSetup(final ServerSetup theSetup) {
        return new ServerSetup(mPortOffset + theSetup.getPort(),
                mHostname,
                theSetup.getProtocol());
    }

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     *
     * @throws Exception in case of shutdown errors. Exceptions will get logged but not rethrown to
     *                   allow other beans to release their resources too.
     */
    public void destroy() throws Exception {
        mGreenMail.stop();
    }


    /**
     * Sets the autostart flag.
     *
     * @param theAutostart the flag.
     */
    public void setAutostart(final boolean theAutostart) {
        mAutostart = theAutostart;
    }


    /**
     * Setter for property 'smtpProtocoll'.
     *
     * @param theSmtpProtocoll Value to set for property 'smtpProtocoll'.
     */
    public void setSmtpProtocoll(final boolean theSmtpProtocoll) {
        mSmtpProtocoll = theSmtpProtocoll;
    }

    /**
     * Setter for property 'smtpsProtocoll'.
     *
     * @param theSmtpsProtocoll Value to set for property 'smtpsProtocoll'.
     */
    public void setSmtpsProtocoll(final boolean theSmtpsProtocoll) {
        mSmtpsProtocoll = theSmtpsProtocoll;
    }

    /**
     * Setter for property 'pop3Protocoll'.
     *
     * @param thePop3Protocoll Value to set for property 'pop3Protocoll'.
     */
    public void setPop3Protocoll(final boolean thePop3Protocoll) {
        mPop3Protocoll = thePop3Protocoll;
    }

    /**
     * Setter for property 'pop3sProtocoll'.
     *
     * @param thePop3sProtocoll Value to set for property 'pop3sProtocoll'.
     */
    public void setPop3sProtocoll(final boolean thePop3sProtocoll) {
        mPop3sProtocoll = thePop3sProtocoll;
    }

    /**
     * Setter for property 'imapProtocoll'.
     *
     * @param theImapProtocoll Value to set for property 'imapProtocoll'.
     */
    public void setImapProtocoll(final boolean theImapProtocoll) {
        mImapProtocoll = theImapProtocoll;
    }

    /**
     * Setter for property 'imapsProtocoll'.
     *
     * @param theImapsProtocoll Value to set for property 'imapsProtocoll'.
     */
    public void setImapsProtocoll(final boolean theImapsProtocoll) {
        mImapsProtocoll = theImapsProtocoll;
    }

    public void setHostname(String hostName){
        this.mHostname=hostName;
    }

    public void setPortOffset(int portOffset){
        this.mPortOffset=portOffset;
    }

    /**
     * Setter for property 'users'.
     *
     * @param theUsers Value to set for property 'users'.
     */
    public void setUsers(final List theUsers) {
        mUsers = theUsers;
    }

    public GreenMail getGreenMail(){
        return mGreenMail;
    }
    public void start(){
        mGreenMail.start();
    }
    public void stop(){
        mGreenMail.stop();
    }
}

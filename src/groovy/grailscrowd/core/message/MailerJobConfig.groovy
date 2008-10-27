package grailscrowd.core.message
/**
 * @author ap
 */
class MailerJobConfig implements MailerJobConfigMBean{
    
    int startDelay = 6000
    
    int timeout = 60000     // execute job every minute

    private boolean enabled = true


    public int getStartDelayInMillis(){
        return startDelay
    }

    public void setStartDelayInMillis(int value){
        this.startDelay=value
    }

    public int getTimeoutInMillis(){
        return timeout
    }

    public void setTimeoutInMillis(int value){
         this.timeout = value
    }
    public void setEnabled(boolean value){
        this.enabled = value
    }
    
    public boolean isEnabled(){
        return enabled
    }

    public void disable(){
       enabled = false
    }

    public void enable(){
        enabled =true
    }
}
package grailscrowd.core.message

import java.util.concurrent.atomic.AtomicInteger

/**
 * To prevent flooding accounts, any outgoing mailaddress must not
 * be in this regExp blacklist.
 *
 * @author ap
 */
class Blacklist implements BlacklistMBean{

    private final AtomicInteger counter = new AtomicInteger(0)

    /** regExp list initialized with our test addresses */
    private final Set blacklistRegExps = (new HashSet()).asSynchronized()


    /**
     * Set initial blacklist values.
     * @param blacklistRegExps values to add 
     */
    public void setBlacklistRegExps(Collection blacklistRegExps){
        this.blacklistRegExps.addAll(blacklistRegExps)
    }


    /**
     * {@inheritDoc }
     */
    public void addRegExpToBlacklist(String regExp) {
        if (regExp&&regExp.trim()){
            blacklistRegExps.add(regExp.trim())
        }
    }


    /**
     * {@inheritDoc }
     */
    public void removeRegExpFromBlacklist(String regExp) {
        blacklistRegExps.remove(regExp)
    }

    /**
     * {@inheritDoc }
     */
    public boolean isInBlacklist(String emailAddressToCheck) {
        boolean result = blacklistRegExps.any {emailAddressToCheck ==~ it}
        if (result){
            counter.incrementAndGet()
        }
        return result
    }

    /**
     * {@inheritDoc }
     */
    public String getBlacklistElements() {
        return blacklistRegExps.join(', ')
    }

    /**
     * {@inheritDoc }
     */
    public int getHitQuantity(){
        return counter.get()
    }
    

}
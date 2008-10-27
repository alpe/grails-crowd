package grailscrowd.core.message

/**
 * To prevent flooding accounts, any outgoing mailaddress must not
 * be in this regExp blacklist.
 *
 * @author ap
 */
class Blacklist implements BlacklistMBean{

    /** regExp list initialized with our test addresses */
    private final List blacklistRegExps = [".*@example.com", "gash@gmx.de"].asSynchronized()


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
        return blacklistRegExps.any {emailAddressToCheck ==~ it}

    }
    
    /**
     * {@inheritDoc }
     */
    public List getBlacklistElements() {
        return blacklistRegExps.asImmutable()
    }

}
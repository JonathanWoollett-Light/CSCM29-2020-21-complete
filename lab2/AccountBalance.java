import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

// AccountBalance defines the balance of the users at a given time
//  in the ledger model of bitcoins.
public class AccountBalance {
    // The current balance of each user, with each account's name mapped to its
    //  current balance.
    private TreeMap<String, Integer> accountBalanceBase;

    // Constructor for creating empty AccountBalance.
    public AccountBalance() {
        accountBalanceBase = new TreeMap<String, Integer>();
    }

    // Constructor for creating AccountBalance from a map from string to integers.
    public AccountBalance(TreeMap<String, Integer> accountBalanceBase) {
        this.accountBalanceBase = accountBalanceBase;
    }

    // Obtain the underlying Treemap from string to integers.
    public TreeMap<String, Integer> getAccountBalanceBase() {
        return accountBalanceBase;
    }

    // Obtain the list of users in the tree map.
    public Set<String> getUsers() {
        return getAccountBalanceBase().keySet();
    }

    // Adds an account for user with balance.
    // <p>
    // if there was an entry it is overridden.
    public void addAccount(String user, int balance) {
        accountBalanceBase.put(user, balance);
    }

    // @return true if the {@code user} exists in AccountBalance
    public boolean hasUser(String user) {
        return accountBalanceBase.containsKey(user);
    }

    // @return the balance for this account {@code account}
    // <p>
    // if there was no entry, return zero
    public int getBalance(String user) {
        if (hasUser(user)) {
            return accountBalanceBase.get(user);
        } else {
            return 0;
        }
    }

    // Set the balance for {@code user} to {@code amount}
    //  this will override any existing entries.
    public void setBalance(String user, int amount) {
        accountBalanceBase.put(user, amount);
    }

    // Adds amount to balance for {@code user}
    // <p>
    // if there was no entry for {@code user} add one with
    // {@code balance}
    public void addBalance(String user, int amount) {
        setBalance(user, getBalance(user) + amount);
    }

    // Subtracts amount from balance for {@code user}
    public void subtractBalance(String user, int amount) {
        setBalance(user, getBalance(user) - amount);
    }

    // Check balance has at least amount for {@code user}
    public boolean checkBalance(String user, int amount) {
        return (getBalance(user) >= amount);
    }

    /**
     *
     *  Task 1: Fill in the body of method checkAccountBalanceDeductable()
     *          It has been commented out so that the code compiles.
     *
     * Check all items in accountBalance2 can be deducted from the current one
     *
     *   accountBalance2 is usually obtained
     *   from a list of inputs of a transaction
     *
     * Checking that a TxOutputList  can be deducted will be later done
     *  by first converting that TxOutputList into an AccountBalance and then using
     *    this method
     *
     * A naive check would just check whether each entry of a TxOutputList can be 
     *   deducted
     *
     * But there could be an output for the same user Alice of say 10 units twice
     *   where there are not enough funds to deduct it twice but enough
     *   funds to deduct it once
     * The naive check would succeed, but after converting the TxOutputList
     *  to AccountBalance we have that for Alice 20 units have to be deducted
     *  so the deduction of the accountBalance created fails.
     *
     * One could try for checking that one should actually deduct each entry in squence
     *   but then one has to backtrack again.
     * Converting the TxOutputList into a AccountBalance is a better approach since the
     *   TxOutputList is usually much smaller than the main AccountBalance.
     *
     * Note amountToCheckForDeduction is not a 2nd amount but the 
     *    but is intended to be a TxEntryListList converted into an 
     *    AccountBalance to check whether it can be deducted.
     */
//    public boolean checkAccountBalanceDeductable(AccountBalance amountToCheckForDeduction){
//	    // ...
//    }

    /**
     *
     *  Task 2: Fill in the method checkTxELdeductable 
     *          It has been commented out so that the code compiles.
     *
     *  It checks that a list of txEntries (which will be inputs of a transactions)
     *     can be deducted from AccountBalance
     *
     *   done by first converting the list of txEntries into an accountBalance
     *     and then checking that the resulting accountBalance can be deducted.
     *
     */
//    public boolean checkTxELdeductable(TxEntryList txel){
//	    // ...
//    }

    /**
     * Task 3: Fill in the methods subtractTxEL and  addTxEL.
     * <p>
     * Subtract a list of txEntries (txel, usually transaction inputs) from the accountBalance
     * <p>
     * requires that the list to be deducted is deductable.
     */
    public void subtractTxEL(TxEntryList txel) {
        // ...
    }

    /**
     * Add a list of txEntries (txel, usually transaction outputs) to the current accountBalance
     */
    public void addTxEL(TxEntryList txel) {
        // ...
    }

    /**
     *
     *  Task 4: Fill in the method checkTxValid
     *          It has been commented out so that the code compiles.
     *
     * Check a transaction is valid:
     *    the sum of outputs is less than the sum of inputs
     *    and the inputs can be deducted from the accountBalance.
     *
     */
//    public boolean checkTxValid(Tx tx){
//	    // ...
//    }

    /**
     * Task 5: Fill in the method processTx
     * <p>
     * Process a transaction
     * by first deducting all the inputs
     * and then adding all the outputs.
     */

//    public void processTx(Tx tx) {
//        // ...
//    }

    /**
     * Prints the current state of the accountBalance.
     */
    public void print() {
        for (String user : getUsers()) {
            Integer value = getBalance(user);
            System.out.println("The balance for " + user + " is " + value);
        }
    }

    /**
     * Task 6: Fill in the testcases as described in the labsheet
     * <p>
     * Testcase
     */
    public static void test() {
        // ...
    }

    /**
     * main function running test cases
     */
    public static void main(String[] args) {
        AccountBalance.test();
    }
}

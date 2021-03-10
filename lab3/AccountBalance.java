import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;


/**
 * AccountBalance defines an accountBalance in the ledger model of bitcoins
 */

public class AccountBalance {

    /**
     * The current accountBalance, with each account's public Key mapped to its
     * account balance.
     */

    private Hashtable<PublicKey, Integer> accountBalanceBase;

    /**
     * In order to print out the accountBalance in a good order
     * we maintain a list of public Keys,
     * which will be the set of public keys maped by it in the order
     * they were added
     **/

    private ArrayList<PublicKey> publicKeyList;


    /**
     * Creates a new accountBalance
     */
    public AccountBalance() {
        accountBalanceBase = new Hashtable<PublicKey, Integer>();
        publicKeyList = new ArrayList<PublicKey>();
    }

    // Creates a new accountBalance from a map from string to integers
    public AccountBalance(Hashtable<PublicKey, Integer> accountBalanceBase) {
        this.accountBalanceBase = accountBalanceBase;
        publicKeyList = new ArrayList<PublicKey>();
        for (PublicKey pbk : accountBalanceBase.keySet()) {
            publicKeyList.add(pbk);
        }
    }

    // Testcase
    public static void test()
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        Wallet exampleWallet = SampleWallet.generate(new String[]{"Alice"});
        byte[] exampleMessage = KeyUtils.integer2ByteArray(1);
        byte[] exampleSignature = exampleWallet.signMessage(exampleMessage, "Alice");


        /**   Task 5
         * Add  to the test case the test as described in the lab sheet.
         * You can use the above exampleSignature, when a sample signature is needed
         *  which cannot be computed from the data.
         */

        // Create  a  sample  wallet  for  Alice  containing  keys  with  names  A1,  A2,
        //  for  Bob containing key names B1, B2, for Carol containing key names C1, C2, C3,
        //  and forDavid containing key name D1 by using the method generate of SampleWallet.
        Wallet aliceWallet = SampleWallet.generate(new String[]{"A1","A2"});
        Wallet bobWallet = SampleWallet.generate(new String[]{"B1","B2"});
        Wallet carolWallet = SampleWallet.generate(new String[]{"C1","C2","C3"});
        Wallet davidWallet = SampleWallet.generate(new String[]{"D1"});

        // Compute the PublicKeyMap containing the public keys of all these wallets.
        // ThePublicKeyMap is for convenience, since comparing public keys is cumbersome.
        PublicKeyMap keyMap = new PublicKeyMap();
        keyMap.addPublicKeyMap(aliceWallet.toPublicKeyMap());
        keyMap.addPublicKeyMap(bobWallet.toPublicKeyMap());
        keyMap.addPublicKeyMap(carolWallet.toPublicKeyMap());
        keyMap.addPublicKeyMap(davidWallet.toPublicKeyMap());

        // Create an empty AccountBalance and add to it the key names of the wallets created
        //  before initialised with the amount 0 for each key.
        AccountBalance accBalance = new AccountBalance();
        accBalance.setBalance(keyMap.getPublicKey("A1"),0);
        accBalance.setBalance(keyMap.getPublicKey("A2"),0);
        accBalance.setBalance(keyMap.getPublicKey("B1"),0);
        accBalance.setBalance(keyMap.getPublicKey("B2"),0);
        accBalance.setBalance(keyMap.getPublicKey("C1"),0);
        accBalance.setBalance(keyMap.getPublicKey("C2"),0);
        accBalance.setBalance(keyMap.getPublicKey("C3"),0);
        accBalance.setBalance(keyMap.getPublicKey("D1"),0);
        accBalance.print(keyMap);
        System.out.println();

        // Set the balance for A1 to 20.
        accBalance.setBalance(keyMap.getPublicKey("A1"),20);
        accBalance.print(keyMap);
        System.out.println();

        // Add 15 to the balance for B1.
        accBalance.addToBalance(keyMap.getPublicKey("B1"),15);
        accBalance.print(keyMap);
        System.out.println();

        // Subtract 5 from the balance for B1.
        accBalance.subtractFromBalance(keyMap.getPublicKey("B1"),5);
        accBalance.print(keyMap);
        System.out.println();

        // Set the balance for C1 to 10.
        accBalance.setBalance(keyMap.getPublicKey("C1"),10);
        accBalance.print(keyMap);
        System.out.println();

        // Check whether the TxInputList txil1 giving A1 15 units, and B1 5 units
        // (with sample signatures used) can be deducted.

        // `TxInputList` requires specification of sender/s, this spec does not note the
        //  sender/s, only the recipients. Given recipients are optional, and senders are not.
        //
        // I presume this is an error and instead should say:
        //  'Check whether the TxInputList txil1 of A1 giving 15 units, and B1 giving 5 units
        //  (with sample signatures used) can be deducted from your constructed AccountBalance.'
        TxInputList txil1 = new TxInputList(
            keyMap.getPublicKey("A1"),15,exampleSignature,
            keyMap.getPublicKey("B1"),5,exampleSignature
        );
        boolean txil1_deductible = accBalance.checkTxInputListCanBeDeducted(txil1);
        System.out.println("txil1: " + txil1_deductible);
        System.out.println();

        // Check whether theTxInputList txil2 giving A1 15 units, and giving A1 again 15 units
        //  can be deducted.
        //
        // Same wording issues as previously noted, to construct a minimal `TxInputList` requires
        //  specification of senders by their public keys not recipients.
        TxInputList txil2 = new TxInputList(
                keyMap.getPublicKey("A1"),15,exampleSignature,
                keyMap.getPublicKey("A1"),15,exampleSignature
        );
        boolean txil2_deductible = accBalance.checkTxInputListCanBeDeducted(txil2);
        System.out.println("txil2: " + txil2_deductible);
        System.out.println();

        // Deduct txil1 from the AccountBalance.
        int bal_a1 = accBalance.getBalance(keyMap.getPublicKey("A1"));
        int bal_b1 = accBalance.getBalance(keyMap.getPublicKey("B1"));
        accBalance.subtractTxInputList(txil1);
        System.out.println("txil1 deducted from account balance:");
        System.out.println("\tA1: " + bal_a1 + " -> " + accBalance.getBalance(keyMap.getPublicKey("A1")));
        System.out.println("\tB1: " + bal_b1 + " -> " + accBalance.getBalance(keyMap.getPublicKey("B1")));
        accBalance.print(keyMap);
        System.out.println();

        // Create a TxOutputList corresponding to txil2 which gives A1 twice 15 Units,
        //  and add it to the AccountBalance.
        //
        // Keys within the TxOutputList class are noted as `sender`, considering this
        //  entire class refers to recipients, I find this rather confusing.
        // They should not be called `sender1`, `sender2`, etc. but rather they should
        //  be called `recipient1`, `recipient2`, etc.
        // I'm not sure how one would create it such that it is 'corresponding to txil2'.
        int bal = accBalance.getBalance(keyMap.getPublicKey("A1"));
        TxOutputList txil2_out = new TxOutputList(
            keyMap.getPublicKey("A1"),15,
            keyMap.getPublicKey("A1"),15
        );
        accBalance.addTxOutputList(txil2_out);
        System.out.println("txil2 added to account balance");
        System.out.println("\tA1: " + bal + " -> " + accBalance.getBalance(keyMap.getPublicKey("A1")));
        accBalance.print(keyMap);
        System.out.println();

        // Create a correctly signed input, where A1 is spending 30, referring to an output list
        //  giving B2 10 and C1 20.
        // The output list is needed in order to create the message to be
        //  signed (consisting of A1 spending 30, B1 receiving 10 and C1 receiving 20).
        // Check whether the signature is valid for this signed input.
        //
        // I don't think the output list is 'consisting of A1 spending 30', I believe this is
        //  specified in the input list, this wording again is rather confusing.
        TxOutputList out1 = new TxOutputList(
            keyMap.getPublicKey("B1"),10,
            keyMap.getPublicKey("C1"),20
        );
        byte[] mes1 = out1.getMessageToSign(keyMap.getPublicKey("A1"),30);
        byte[] sig1 = aliceWallet.signMessage(mes1, "A1");
        TxInputList in1 = new TxInputList(
            keyMap.getPublicKey("A1"),30,sig1
        );
        boolean valid1 = in1.checkSignature(out1);
        System.out.println("valid1: " + valid1);
        System.out.println();

        // Create a wrongly signed input, which gives A1 30, and uses instead of the correctly
        //  created signature an example signature (example signatures are provided in the code).
        // Check whether the signature is valid for this signed input.
        //
        // 'a wrongly signed input, which gives A1 30' inputs do not give anyone anything,
        //  should A1 be given 30 in the output, or should A1 give 30 in the input?
        // I presume this is effectively 'Copy what you did before but use an invalid signature'
        TxOutputList out2 = new TxOutputList(
                keyMap.getPublicKey("B1"),10,
                keyMap.getPublicKey("C1"),20
        );
        TxInputList in2 = new TxInputList(
                keyMap.getPublicKey("A1"),30,exampleSignature
        );
        boolean valid2 = in2.checkSignature(out2);
        System.out.println("valid2: " + valid2);
        System.out.println();

        // Create a transaction tx1 which takes as input for A1 35 units and gives B2 10,C2 10,
        //  and returns the change (whatever is left) to A2.
        TxOutputList tx1_out = new TxOutputList(
            keyMap.getPublicKey("B2"),10,
            keyMap.getPublicKey("C2"),10,
            keyMap.getPublicKey("A2"),15 // 15 = 35 - 10 - 10
        );
        byte[] tx1_mes = tx1_out.getMessageToSign(keyMap.getPublicKey("A1"),35);
        byte[] tx1_sig = aliceWallet.signMessage(tx1_mes, "A1");
        TxInputList tx1_in = new TxInputList(
                keyMap.getPublicKey("A1"),35,tx1_sig
        );
        Transaction tx1 =  new Transaction(tx1_in,tx1_out);

        // Check whether the signature is approved for the transaction input, and whether the
        //  transaction is valid.
        // Then update the AccountBalance using that transaction.
        boolean tx1_in_approved = tx1_in.checkSignature(tx1_out);
        System.out.println("tx1_in_approved: " + tx1_in_approved);
        boolean tx1_valid = tx1.checkTransactionAmountsValid() && tx1.checkSignaturesValid();
        System.out.println("tx1_valid: " + tx1_valid);
        accBalance.processTransaction(tx1);
        System.out.println();
    }

    // main function running test cases
    public static void main(String[] args)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        AccountBalance.test();
    }

    // obtain the underlying Hashtable from string to integers
    public Hashtable<PublicKey, Integer> getAccountBalanceBase() {
        return accountBalanceBase;
    }

    // obtain the list of publicKeys in the tree map
    public Set<PublicKey> getPublicKeys() {
        return getAccountBalanceBase().keySet();
    }

    // obtain the list of publicKeys in the order they were added
    public ArrayList<PublicKey> getPublicKeysOrdered() {
        return publicKeyList;
    }

    // Adds a mapping from new account's name {@code publicKey} to its
    //  account balance {@code balance} into the accountBalance.
    // If there was an entry it is overridden.
    public void addAccount(PublicKey publicKey, int balance) {
        accountBalanceBase.put(publicKey, balance);
        if (!publicKeyList.contains(publicKey)) {
            publicKeyList.add(publicKey);
        }
    }

    // @return true if the {@code publicKey} exists in the accountBalance.
    public boolean hasPublicKey(PublicKey publicKey) {
        return accountBalanceBase.containsKey(publicKey);
    }

    // @return the balance for this account {@code account}, if there was no entry, return zero
    public int getBalance(PublicKey publicKey) {
        if (hasPublicKey(publicKey)) {
            return accountBalanceBase.get(publicKey);
        } else {
            return 0;
        }
    }

    // set the balance for {@code publicKey} to {@code amount}
    public void setBalance(PublicKey publicKey, int amount) {
        accountBalanceBase.put(publicKey, amount);
        if (!publicKeyList.contains(publicKey)) {
            publicKeyList.add(publicKey);
        }
    }

    // Increments Adds amount to balance for {@code publicKey}.
    // If there was no entry for {@code publicKey} add one with
    // {@code balance}
    public void addToBalance(PublicKey publicKey, int amount) {
        setBalance(publicKey, getBalance(publicKey) + amount);
    }


    /* checks whether an accountBalance can be deducted 
       this is an auxiliary function used to define checkTxInputListCanBeDeducted */

    // Subtracts amount from balance for {@code publicKey}
    public void subtractFromBalance(PublicKey publicKey, int amount) {
        setBalance(publicKey, getBalance(publicKey) - amount);
    }

    // Check balance has at least amount for {@code publicKey}
    public boolean checkBalance(PublicKey publicKey, int amount) {
        return (getBalance(publicKey) >= amount);
    }

    public boolean checkAccountBalanceCanBeDeducted(AccountBalance accountBalance2) {
        for (PublicKey publicKey : accountBalance2.getPublicKeys()) {
            if (getBalance(publicKey) < accountBalance2.getBalance(publicKey))
                return false;
        }
        return true;
    }

    /**
     * Check that a list of publicKey amounts can be deducted from the
     * current accountBalance
     * <p>
     * done by first converting the list of publicKey amounts into an accountBalance
     * and then checking that the resulting accountBalance can be deducted.
     */
    public boolean checkTxInputListCanBeDeducted(TxInputList txInputList) {
        return checkAccountBalanceCanBeDeducted(txInputList.toAccountBalance());
    }

    // Subtract a list of TxInput from the accountBalance
    // Requires that the list to be deducted is deductable.
    public void subtractTxInputList(TxInputList txInputList) {
        for (TxInput entry : txInputList.toList()) {
            subtractFromBalance(entry.getSender(), entry.getAmount());
        }
    }

    // Adds a list of txOutput of a transaction to the current accountBalance
    public void addTxOutputList(TxOutputList txOutputList) {
        for (TxOutput entry : txOutputList.toList()) {
            addToBalance(entry.getRecipient(), entry.getAmount());
        }
    }

    /**
     * Task 4 Check a transaction is valid:
     * - The sum of outputs <= sum of the inputs.
     * - All signatures are valid.
     * - Inputs can be deducted from the accountBalance.
     *
     * This method has been set to true so that the code compiles - that should
     *  be changed
     */
    public boolean checkTransactionValid(Transaction tx) {
        return (
            // The sum of outputs <= sum of the inputs.
            tx.toTxInputs().toAccountBalance().checkAccountBalanceCanBeDeducted(
               tx.toTxOutputs().toAccountBalance()
            ) &&
            // All signatures are valid.
            tx.checkSignaturesValid() &&
            // Inputs can be deducted from the accountBalance.
            this.checkTxInputListCanBeDeducted(tx.toTxInputs())
        );

	    // This is not the correct value, only used here so that the code compiles.
        // return true;
    }

    // Process a transaction.
    // Deduct inputs then adding outputs.
    public void processTransaction(Transaction tx) {
        subtractTxInputList(tx.toTxInputs());
        addTxOutputList(tx.toTxOutputs());
    }

    // Prints the current state of the accountBalance.
    public void print(PublicKeyMap pubKeyMap) {
        for (PublicKey publicKey : publicKeyList) {
            Integer value = getBalance(publicKey);
            System.out.println("The balance for " +
                    pubKeyMap.getUser(publicKey) + " is " + value);
        }

    }
}

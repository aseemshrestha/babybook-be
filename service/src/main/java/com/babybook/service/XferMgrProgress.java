package com.babybook.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferProgress;

public class XferMgrProgress
{
    public static void waitForCompletion(Transfer xfer)
    {

        try {
            xfer.waitForCompletion();
        } catch (AmazonServiceException e) {
            System.err.println("Amazon service error: " + e.getMessage());
            System.exit(1);
        } catch (AmazonClientException e) {
            System.err.println("Amazon client error: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("Transfer interrupted: " + e.getMessage());
            System.exit(1);
        }

    }

    // Prints progress while waiting for the transfer to finish.
    public static void showTransferProgress(Transfer xfer)
    {
        // print the transfer's human-readable description
        System.out.println(xfer.getDescription());
        // update the progress bar while the xfer is ongoing.
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
            // Note: so_far and total aren't used, they're just for
            // documentation purposes.
            TransferProgress progress = xfer.getProgress();
            long so_far = progress.getBytesTransferred();
            long total = progress.getTotalBytesToTransfer();
            double pct = progress.getPercentTransferred();
        } while (xfer.isDone() == false);
        // print the final state of the transfer.
        Transfer.TransferState xfer_state = xfer.getState();
        System.out.println(": " + xfer_state);

    }
}

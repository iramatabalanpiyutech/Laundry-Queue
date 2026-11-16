
package App.Dubuggers;

import Service.Managers.MachineAssignmentManager;
import java.util.Scanner;

public class Quarantine {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {

        MachineAssignmentManager mam = new MachineAssignmentManager();

        int loadId = 91;
        int washerAssignmentId = 11;
        int dryerAssignmentId = 12;
        int dryerId = 31;

        System.out.println("=== FULL WASH INTEGRATION TEST ===");

        // STEP 1: Washer assignment already exists
        System.out.println("\n[1] Washer Assignment: 11");
        pause();

        // STEP 2: Start washing
        mam.startSession(washerAssignmentId);
        System.out.println("[2] Washing started!");
        pause();

        // STEP 3: Wait for washing
        waitForCompletion(mam, washerAssignmentId);

        // STEP 4: Complete washing
        mam.completeSession(washerAssignmentId);
        System.out.println("[3] Washing completed!");
        System.out.println("Expected load state: TRANSFER");
        pause();

        // STEP 5: Transfer to dryer
        mam.transferLoad(loadId, dryerId);
        System.out.println("[4] Dryer assignment created!");
        System.out.println("Expected assignment ID: 12");
        System.out.println("Expected load state: QUEUED_DRYER");
        pause();

        // STEP 6: Start dryer
        mam.startSession(dryerAssignmentId);
        System.out.println("[5] Drying started!");
        pause();

        // STEP 7: Wait for drying
        waitForCompletion(mam, dryerAssignmentId);

        // STEP 8: Complete dryer
        mam.completeSession(dryerAssignmentId);
        System.out.println("[6] Drying completed!");
        System.out.println("Expected load state: COMPLETED");

        System.out.println("\n=== FULL WASH TEST FINISHED ===");
    }

    private static void pause() {
        System.out.println("\nPress ENTER to continue...");
        scanner.nextLine();
    }

    private static void waitForCompletion(
            MachineAssignmentManager mam,
            int assignmentId
    ) throws InterruptedException {

        long remaining;

        do {
            remaining = mam.getRemainingSeconds(assignmentId);

            System.out.printf(
                    "\rTime Remaining: %02d:%02d",
                    remaining / 60,
                    remaining % 60
            );

            System.out.flush();

            if (remaining > 0) {
                Thread.sleep(1000);
            }

        } while (remaining > 0);

        System.out.println();
    }
}

package Service.DataManager.Repo;

import Model.MachineAssignment;
import Service.DataManager.DataConnections;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MachineAsignmentRepo implements RepoSpecificallyForMachineAss<MachineAssignment> {

    @Override
    public void addEntry(MachineAssignment item) {
        String sql = "INSERT INTO MACHINE_ASSIGNMENTS (LOAD_ID, MACHINE_ID, STARTED_AT, DURATION_MINUTES, ENDED_AT) " +
                "VALUES (?, ?, ?, ?, ?)";

        try(
                Connection maConn = DataConnections.getConnection();
                PreparedStatement maPs = maConn.prepareStatement(sql);
                ){
            maPs.setInt(1,item.getLoadId());
            maPs.setInt(2,item.getMachineId());
            maPs.setTimestamp(3,
                    item.getStartedAt() == null
                            ? null
                            : Timestamp.valueOf(item.getStartedAt())
            );

            maPs.setInt(4,item.getDurationMinutes());
            maPs.setTimestamp(5,
                    item.getEndedAt() == null
                            ? null
                            : Timestamp.valueOf(item.getEndedAt())
            );

            int row = maPs.executeUpdate();
            System.out.println("Rows Affected: " + row);
        }catch(SQLException e){
            throw new RuntimeException("Failed to insert machine assignment", e);
        }
        
    }

    @Override
    public void updateEntry(MachineAssignment item) {
        String sql = "UPDATE MACHINE_ASSIGNMENTS SET DURATION_MINUTES = ? WHERE ASSIGNMENT_ID = ?";
            try (
                Connection conn = DataConnections.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
            ) {
                // Set parameters here
                ps.setInt(1, item.getDurationMinutes());

                int rows = ps.executeUpdate();
                System.out.println("Rows affected: " + rows);

            } catch (SQLException e) {
                throw new RuntimeException("Failed to update machine assignment", e);
            }

    }

    @Override
    public void updateStateStart(MachineAssignment item) {
        String sql = "UPDATE MACHINE_ASSIGNMENTS SET STARTED_AT = ? WHERE ASSIGNMENT_ID = ?";
        try (
                Connection conn = DataConnections.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // Set parameters here
            ps.setTimestamp(1, Timestamp.valueOf(item.getStartedAt()));
            ps.setInt(2, item.getAssignmentId());

            int rows = ps.executeUpdate();
            System.out.println("Rows affected: " + rows);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to start machine assignment", e);
        }

    }

    @Override
    public void updateStateEnd(MachineAssignment item) {
        String sql = "UPDATE MACHINE_ASSIGNMENTS SET ENDED_AT = ? WHERE ASSIGNMENT_ID = ?";
        try (
                Connection conn = DataConnections.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // Set parameters here
            ps.setTimestamp(1, Timestamp.valueOf(item.getEndedAt()));
            ps.setInt(2, item.getAssignmentId());

            int rows = ps.executeUpdate();
            System.out.println("Rows affected: " + rows);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to end machine assignment", e);
        }
    }


    @Override
    public void deleteEntry(int id) {
        String sql = "DELETE FROM MACHINE_ASSIGNMENTS WHERE ASSIGNMENT_ID = ?";
        try (
            Connection conn = DataConnections.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // Set parameters here
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            System.out.println("Rows affected: " + rows);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete machine assignment", e);
        }

    }

    //For records
    @Override
    public MachineAssignment findById(int id) {

        String sql = "SELECT * FROM MACHINE_ASSIGNMENTS WHERE ASSIGNMENT_ID = ?";

        try (
                Connection conn = DataConnections.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            // IMPORTANT: Bind the ID before executing
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    // Retrieve timestamps
                    Timestamp ts = rs.getTimestamp("STARTED_AT");
                    Timestamp es = rs.getTimestamp("ENDED_AT");

                    // Convert to LocalDateTime
                    LocalDateTime startedAt = ts != null
                            ? ts.toLocalDateTime()
                            : null;

                    LocalDateTime endedAt = es != null
                            ? es.toLocalDateTime()
                            : null;

                    // Return the object directly
                    return new MachineAssignment(
                            rs.getInt("ASSIGNMENT_ID"),
                            rs.getInt("LOAD_ID"),
                            rs.getInt("MACHINE_ID"),
                            startedAt,
                            rs.getInt("DURATION_MINUTES"),
                            endedAt
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find machine assignment", e);
        }

        return null;
    }

    @Override
    public List<MachineAssignment> findAll() {
        List<MachineAssignment> ma = new ArrayList<>();

        String sql = "SELECT * FROM MACHINE_ASSIGNMENTS";

        try (
            Connection conn = DataConnections.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                        // Retrieve timestamps
                        Timestamp ts = rs.getTimestamp("STARTED_AT");
                        Timestamp es = rs.getTimestamp("ENDED_AT");

                        // Convert to LocalDateTime
                        LocalDateTime startedAt = ts != null
                                ? ts.toLocalDateTime()
                                : null;

                        LocalDateTime endedAt = es != null
                                ? es.toLocalDateTime()
                                : null;

                        // Create and add MachineAssignment
                        ma.add(new MachineAssignment(
                                rs.getInt("ASSIGNMENT_ID"),
                                rs.getInt("LOAD_ID"),
                                rs.getInt("MACHINE_ID"),
                                startedAt,
                                rs.getInt("DURATION_MINUTES"),
                                endedAt
                        ));
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException("Failed to load machine assignments", e);
        }

        return ma;
    }

    @Override
    public boolean hasActiveLoadAssignment(int id) {
        String sql = "SELECT COUNT(*) FROM MACHINE_ASSIGNMENTS WHERE LOAD_ID = ? AND ENDED_AT IS NULL";
        try(
                Connection conn = DataConnections.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
                ){
            ps.setInt(1,id);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean hasActiveMachineAssignment(int id) {
        String sql = "SELECT COUNT(*) FROM MACHINE_ASSIGNMENTS WHERE MACHINE_ID = ? AND ENDED_AT IS NULL";
        try (
                Connection conn = DataConnections.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}

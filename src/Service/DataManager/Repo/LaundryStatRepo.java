package Service.DataManager.Repo;


import Model.Customer;
import Model.Load;
import Model.ServiceStage;
import Model.ServiceType;
import Service.DataManager.DataConnections;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LaundryStatRepo implements AssRepo<Load> {
    CustomerRepo cr = new CustomerRepo();
    List<Customer> lCst = cr.findAll();
    Customer selected = lCst.get(0);

    @Override
    public void addEntry(Load item) {
        String sql = "INSERT INTO LAUNDRY_LOADS(CUSTOMER_ID, WEIGHT_KG, QUEUE_STATUS, RECEIVED_AT, SERVICE_TYPE) VALUES (?,?,?,?,?)";
        try(
                Connection conn = DataConnections.getConnection();
                PreparedStatement  ps = conn.prepareStatement(sql);
                ){
            ps.setInt(1, item.getCstIdFk());
            ps.setDouble(2,item.getLoadWeight());
            ps.setString(3,item.getLoadStat().toString());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(5, item.getServiceType().name());
            int row = ps.executeUpdate();
            System.out.println("Rows affected: " + row );

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void updateEntry(Load item) {
        String sql = "UPDATE LAUNDRY_LOADS SET WEIGHT_KG = ? WHERE LOAD_ID = ?";

        try (
            Connection conn = DataConnections.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // Set parameters here
            ps.setDouble(1, item.getLoadWeight());
            ps.setInt(2,item.getLoadId());

            int rows = ps.executeUpdate();
            System.out.println("Rows affected: " + rows);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void updateEntryState(Load item) {
        String sql = "UPDATE LAUNDRY_LOADS SET QUEUE_STATUS = ? WHERE LOAD_ID = ?";
        
        try (
            Connection conn = DataConnections.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, item.getLoadStat().toString());
            ps.setInt(2, item.getLoadId());
        
            int rows = ps.executeUpdate();
            System.out.println("Rows affected: " + rows);
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }

    @Override
    public void deleteEntry(int id) {
        String sql = "DELETE LAUNDRY_LOADS WHERE LOAD_ID = ?";
        try(
                Connection con = DataConnections.getConnection();
                PreparedStatement prep = con.prepareStatement(sql);
        ){

            prep.setInt(1, id);
            int rows = prep.executeUpdate();
            System.out.println(rows);

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Load> findAll() {

        String sql = "SELECT LOAD_ID,CUSTOMER_ID,WEIGHT_KG,QUEUE_STATUS,RECEIVED_AT FROM LAUNDRY_LOADS ORDER BY RECEIVED_AT";
        List<Load> loadAss = new ArrayList<>();
        try (
            Connection conn = DataConnections.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {

                while (rs.next()) {
                    Object recieveAt;
                    loadAss.add(new Load(
                            rs.getInt("LOAD_ID"),
                            rs.getInt("CUSTOMER_ID"),
                            rs.getDouble("WEIGHT_KG"),
                            ServiceStage.valueOf(rs.getString("QUEUE_STATUS")),
                            rs.getTimestamp("RECEIVE_AT").toLocalDateTime(),
                            ServiceType.valueOf(rs.getString("SERVICE_TYPE"))
                    ));
                }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return loadAss;
    }

    @Override
    public Load findById(int id) {
        String sql = "SELECT * FROM LAUNDRY_LOADS WHERE LOAD_ID = ?";

        try (
                Connection conn = DataConnections.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String serviceType = rs.getString("SERVICE_TYPE");
                    Timestamp receivedAt = rs.getTimestamp("RECEIVED_AT");

                    return new Load(
                            rs.getInt("LOAD_ID"),
                            rs.getInt("CUSTOMER_ID"),
                            rs.getDouble("WEIGHT_KG"),
                            ServiceStage.valueOf(rs.getString("QUEUE_STATUS")),
                            receivedAt != null ? receivedAt.toLocalDateTime() : null,
                            serviceType != null ? ServiceType.valueOf(serviceType) : null
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}

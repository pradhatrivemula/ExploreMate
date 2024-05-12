using System.Configuration;
using System.Data.SqlClient;

namespace JSONWebAPI
{
    public class DBConnect
    {
        private static SqlConnection NewCon ;
        public static SqlConnection getConnection()
        {
            NewCon = new SqlConnection(@"Data Source=182.50.133.110;Initial Catalog=TravelPack;Persist Security Info=True;User ID=TravelPack;Password=Qud8s*86");
            return NewCon;
        }
        public DBConnect()
        {

        }

    }
}
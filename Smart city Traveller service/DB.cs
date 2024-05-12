using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.SqlClient;
using System.Configuration;

namespace WebApplication2
{
    public class DB
    {
        private static SqlConnection NewCon;  
        private static string conStr = ConfigurationManager.ConnectionStrings["ConString"].ConnectionString;  
        public static SqlConnection getConnection()  
        {  
            NewCon = new SqlConnection(conStr);  
            return NewCon;  
        }  
        public DB()  
        {  
  
        } 
    }
}
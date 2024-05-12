using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.SqlClient;

namespace WebApplication2
{
    public class SAPI : ISAPI
    {

        SqlDataReader dr;
        SqlConnection con;
        SqlCommand cmd;
        public SAPI()
        {
            con = DB.getConnection();
        }

        public string getuid()
        {
            string s = "";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }

            cmd = new SqlCommand("Select Id from Cust ORDER BY Id DESC", con);
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                dr.Read();
                s = dr.GetString(0);
                int i = Convert.ToInt32(s);
                i += 1;
                s = "" + i;
            }
            else
            {
                s = "1000";
            }
            con.Close();
            return s;

        }

        public string autoplaceid()
        {
            string s = "";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }

            cmd = new SqlCommand("Select Id from FinalSection ORDER BY Id DESC", con);
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                dr.Read();
                s = dr.GetString(0);
                int i = Convert.ToInt32(s);
                i += 1;
                s = "" + i;

            }
            else
            {
                s = "100";
            }
            con.Close();
            return s;

        }

        public string login(string email, string pass)
        {
            String s = "false";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }
            cmd = new SqlCommand("select ID from Cust where Email='" + email + "' AND Password='" + pass + "'", con);
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                dr.Read();
                s = dr[0].ToString();
            }
            con.Close();
            return s;
        }

        public string register(string id, string name, string contact, string dob, string email, string pass, string ans1, string ans2, string ans3)
        {
            string s = "false";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }

            cmd = new SqlCommand("insert into Cust values('" + id + "','" + name + "','" + contact + "','" + dob + "','" + email + "','" + pass + "','" + ans1 + "','" + ans2 + "','" + ans3 + "')", con);
            cmd.ExecuteNonQuery();
            con.Close();
            s = "true";
            return s;

        }

        public string checkemail(string email)
        {
            string s = "no";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }

            cmd = new SqlCommand("select * from Cust where Email='" + email + "'", con);
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                s = "yes";
            }
            con.Close();
            return s;

        }

        public string addtempplace(string name, string cont, string lat, string lng, string add, string dist, string isopen, string opentill, string rating, string review, string uid, string price)
        {
            string res = "false";
            string id = "1";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }
            cmd = new SqlCommand("select Id from TemprPlace ORDER BY Id DESC", con);
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                dr.Read();
                int i = Convert.ToInt32(dr.GetString(0));
                i += 1;
                id = "" + i;
            }
            con.Close();

            SqlCommand cmd1 = new SqlCommand("insert into TemprPlace values('" + id + "','" + name + "','" + cont + "','" + lat + "','" + lng + "','" + add + "'," + dist + ",'" + isopen + "','" + opentill + "'," + rating + ",'" + review + "','" + uid + "','" + price + "')", con);
            con.Open();
            cmd1.ExecuteNonQuery();
            con.Close();
            res = "true";
            return res;


        }

        public string gettempdata(string id)
        {
            string s = "false";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }
            cmd = new SqlCommand("select ID, Name, Contact, Lat, Lng, Address,Distance, IsOpen, OpenTill,(Cast(Rating As Float)) AS Exp1, Review, User_ID, Price from TemprPlace where User_ID = '" + id + "' Order By Exp1 DESC", con);
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                s = "";
                while (dr.Read())
                {
                    s += dr[1].ToString() + "*" + dr[2].ToString() + "*" + dr[3].ToString() + "*" + dr[4].ToString() + "*" + dr[5].ToString() + "*" + dr[6].ToString() + "*" + dr[7].ToString() + "*" + dr[8].ToString() + "*" + dr[9].ToString() + "*" + dr[10].ToString() + "*" + dr[12].ToString() + "#";
                }
            }
            else
            {
                s = "na";
            }
            con.Close();

            SqlCommand cmd1 = new SqlCommand("delete from TemprPlace where User_ID='" + id + "'", con);
            con.Open();
            cmd1.ExecuteNonQuery();
            con.Close();

            return s;
        }

        public string getAnsInfo(string id)
        {
            string s = "no";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }
            cmd = new SqlCommand("select Ans1,Ans2,Ans3 from Cust where Id='" + id + "'", con);
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                dr.Read();
                s = dr[0].ToString() + "*" + dr[1].ToString() + "*" + dr[2].ToString();
            }
            con.Close();
            return s;
        }

        public string getplaceid()
        {
            string id = "100";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }
            cmd = new SqlCommand("select TId from FinalSection ORDER BY TId DESC", con);
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                dr.Read();
                int i = Convert.ToInt32(dr.GetString(0));
                i += 1;
                id = "" + i;
            }
            con.Close();
            return id;
        }

        public string addfinalroute(string id, string uid, string placeno, string name, string cont, string lat, string lng, string add, string dist, string isopen, string opentill, string rating, string review, string price, string time_frame, string date)
        {
            string s = "false";
            name = name.Replace('\'', ' ');
            review = review.Replace('\'', ' ');
            add = add.Replace('\'', ' ');

            try
            {
                if (con.State.ToString() == "Closed")
                {
                    con.Open();
                }

                cmd = new SqlCommand("Select * from Places where Name = '" + name + "' AND Address = '" + add + "'", con);
                SqlDataReader dr = cmd.ExecuteReader();
                if (!dr.HasRows)
                {
                    con.Close();
                    cmd = new SqlCommand("Insert into Places Values ('" + name + "','" + cont + "','" + lat + "','" + lng + "','" + add + "','" + dist + "','" + opentill + "','" + rating + "','" + review + "','" + price + "','" + time_frame + "')", con);
                    con.Open();
                    cmd.ExecuteNonQuery();
                }
                con.Close();

                cmd = new SqlCommand("insert into FinalSection values('" + id + "','" + uid + "','" + placeno + "','" + name + "','" + cont + "','" + lat + "','" + lng + "','" + add + "','" + dist + "','" + isopen + "','" + opentill + "','" + rating + "','" + review + "','" + price + "','" + time_frame + "','" + date + "')", con);
                con.Open();
                cmd.ExecuteNonQuery();
                s = "true";
                con.Close();
            }
            catch (Exception ep)
            {
                //s = cmd.CommandText;
                s = ep.ToString();
            }
            return s;
        }

        public string getcurrentroute(string uid, string date)
        {
            string s = "";
            try
            {
                if (con.State.ToString() == "Closed")
                {
                    con.Open();
                }
                cmd = new SqlCommand("select * from FinalSection where User_ID='" + uid + "' AND Date >= '" + date + "' Order By Place_No", con);
                dr = cmd.ExecuteReader();
                if (dr.HasRows)
                {
                    while (dr.Read())
                    {
                        s += dr[0].ToString() + "*" + dr[2].ToString() + "*" + dr[3].ToString() + "*" + dr[4].ToString() + "*" + dr[5].ToString() + "*" + dr[6].ToString() + "*" + dr[7].ToString() + "*" + dr[8].ToString() + "*" + dr[9].ToString() + "*" + dr[10].ToString() + "*" + dr[11].ToString() + "*" + dr[12].ToString() + "*" + dr[13].ToString() + "*" + dr[14].ToString() + "*#";
                    }
                }
                else
                {
                    s = "no";
                }
                con.Close();
            }
            catch (Exception ep)
            {
                s = ep.ToString();
            }
            return s;
        }

        public string getallroutes()
        {
            string s = "";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }
            cmd = new SqlCommand("select * from FinalSection", con);
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                while (dr.Read())
                {
                    s += dr[0].ToString() + "*" + dr[2].ToString() + "*" + dr[3].ToString() + "*" + dr[4].ToString() + "*" + dr[5].ToString() + "*" + dr[6].ToString() + "*" + dr[7].ToString() + "*" + dr[8].ToString() + "*" + dr[9].ToString() + "*" + dr[10].ToString() + "*" + dr[1].ToString() + "*" + dr[12].ToString() + "*" + dr[13].ToString() + "*" + dr[14].ToString();
                }
            }
            else
            {
                s = "no";
            }
            con.Close();
            return s;
        }

        public string delroute(string id)
        {
            string s = "false";
            try
            {
                if (con.State.ToString() == "Closed")
                {
                    con.Open();
                }
                cmd = new SqlCommand("Delete from FinalSection where User_Id='" + id + "'", con);
                cmd.ExecuteNonQuery();
                con.Close();
                s = "true";
            }
            catch (Exception ep)
            {
                s = ep.ToString();
            }
            return s;
        }

        public string feed(string uid, string feed, string date)
        {
            string s = "false";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }
            cmd = new SqlCommand("insert into Feedback values('" + uid + "','" + feed + "','" + date + "')", con);
            cmd.ExecuteNonQuery();
            con.Close();
            s = "true";
            return s;
        }


        public string adddata(string lat, string lng, string date, string time)
        {
            string s = "false";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }
            cmd = new SqlCommand("insert into TempPlace values('" + lat + "','" + lng + "','" + date + "','" + time + "')", con);
            cmd.ExecuteNonQuery();
            con.Close();
            s = "true";
            return s;
        }

        public string getdata(string str)
        {
            string s = "";
            if (con.State.ToString() == "Closed")
            {
                con.Open();
            }

            if (str.CompareTo("all") == 0)
            {
                cmd = new SqlCommand("select * from TempPlace", con);
            }
            else
            {
                cmd = new SqlCommand("select * from TempPlace where Date='"+str+"'", con);
            }
            dr = cmd.ExecuteReader();
            if (dr.HasRows)
            {
                while (dr.Read())
                {
                    s += dr[0].ToString() + "*" + dr[1].ToString() + "*" + dr[2].ToString() + "*" + dr[3].ToString()+"#";
                }
            }
            else
            {
                s = "no";
            }
            con.Close();
            return s;
        }


    }
}
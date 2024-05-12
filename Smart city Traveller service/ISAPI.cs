using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication2
{
    public interface ISAPI
    {
        string getuid();
        string autoplaceid();
        string login(string email, string pass);
        string register(string id, string name, string contact, string dob, string email, string pass, string ans1, string ans2, string ans3);
        string checkemail(string email);
        string addtempplace(string name, string cont, string lat, string lng, string add, string dist, string isopen, string opentill, string rating, string review, string uid, string price);
        string gettempdata(string id);
        string getAnsInfo(string id);


        string getplaceid();
        string addfinalroute(string id, string uid, string placeno, string name, string cont, string lat, string lng, string add, string dist, string isopen, string opentill, string rating, string review, string price, string time_frame, string date);
        string getcurrentroute(string uid, string date);
        string getallroutes();
        string delroute(string id);
        string feed(string uid, string feed, string date);

        string adddata(string lat, string lng, string date, string time);
        string getdata(string str);
    }
}
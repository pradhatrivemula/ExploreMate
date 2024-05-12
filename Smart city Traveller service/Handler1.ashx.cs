using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using JsonServices;
using JsonServices.Web;

namespace WebApplication2
{
    /// <summary>
    /// Summary description for Handler1
    /// </summary>
    public class Handler1 : JsonHandler
    {

        public Handler1()
        {
            this.service.Name = " TravlerAPI ";
            this.service.Description = "JSON API for android appliation";
            InterfaceConfiguration IConfig = new InterfaceConfiguration("RestAPI", typeof(ISAPI), typeof(SAPI));
            this.service.Interfaces.Add(IConfig);
        }
    }
}
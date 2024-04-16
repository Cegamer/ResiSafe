using Microsoft.VisualStudio.TestTools.UnitTesting;
using APIConjuntos.Controllers;
using APIConjuntos.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using APIConjuntos.DTO;
using Microsoft.AspNetCore.Mvc;
using System.Net;
using Microsoft.EntityFrameworkCore;
using Moq;
using Microsoft.Extensions.Options;
using Org.BouncyCastle.Asn1.Ocsp;
using MySqlX.XDevAPI;
using System.Reflection.Metadata;
using System.Text.Json;
using MySqlX.XDevAPI.Common;

namespace APIConjuntos.Controllers.Tests
{
    [TestClass()]
    public class UsersControllerTests : APIConjuntos.Program
    {
        private static readonly HttpClient client = new HttpClient();

        [TestMethod()]
        public async Task PostTest()
        {// Arrange
            var registerInfo = new RegisterDTO("1234", "1234", 54321, "12345");

            // Convert RegisterDTO object to JSON string
            var jsonContent = JsonSerializer.Serialize(registerInfo);

            // Create StringContent with JSON content
            var httpContent = new StringContent(jsonContent, Encoding.UTF8, "application/json");

            // Send POST request with JSON content
            var response = await client.PostAsync("https://localhost:7276/api/Users/Register", httpContent);

            // Check if the request was successful
            if (response.IsSuccessStatusCode)
            {
                Assert.AreEqual((int)HttpStatusCode.OK, ((int)response.StatusCode));

            }
            else
            {
                Assert.Fail();
            }
        }
    }
}
using APIConjuntos.DTO;
using APIConjuntos.Models;
using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Net;
using System.Security.Claims;

namespace APIConjuntos.Controllers
{
    public class ConjuntosController : Controller
    {
        Mapper mapper = new Mapper(new MapperConfiguration(cfg => cfg.CreateMap<Conjunto, ConjuntoDTO>().ReverseMap()));
        appContext dbContext = new appContext();

        // GET: ConjuntosController
        public ActionResult Index()
        {
            return View();
        }

        // GET: ConjuntosController/Details/5
        public ActionResult Details(int id)
        {
            return View();
        }

        // GET: ConjuntosController/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: ConjuntosController/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Authorize]
        public ActionResult Create(ConjuntoDTO conjunto)
        {
            var userId = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            var userProfile = Convert.ToInt32(User.FindFirst(ClaimTypes.Role)?.Value);

            PerfilesDTO perfil = mapper.Map<Perfil, PerfilesDTO>(dbContext.Perfils.First(p => p.IdPerfil == userProfile));


            if (conjunto == null)
            {
                var problemDetails = new ProblemDetails
                {
                    Status = (int)HttpStatusCode.BadRequest,
                    Title = "Los datos no pueden ser nulos",
                    Detail = "No se ha ingresado ningún dato"
                };

                return BadRequest(problemDetails);
            }

            if (perfil.IdTipoPerfil == 1)
            {
                Conjunto conjuntoRegistrar = mapper.Map<ConjuntoDTO, Conjunto>(conjunto);
                dbContext.Conjuntos.Add(conjuntoRegistrar);
                return new JsonResult(new { message = "Conjunto Creado" });
            }

            else
            {
                var problemDetails = new ProblemDetails
                {
                    Status = (int)HttpStatusCode.Unauthorized,
                    Title = "No tiene acceso al recurso",
                    Detail = "LOGIN-03",
                };
                return Unauthorized(problemDetails);
            }
        }

        // GET: ConjuntosController/Edit/5
        public ActionResult Edit(int id)
        {
            return View();
        }

        // POST: ConjuntosController/Edit/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit(int id, IFormCollection collection)
        {
            try
            {
                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return View();
            }
        }

        // GET: ConjuntosController/Delete/5
        public ActionResult Delete(int id)
        {
            return View();
        }

        // POST: ConjuntosController/Delete/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Delete(int id, IFormCollection collection)
        {
            try
            {
                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return View();
            }
        }
    }
}

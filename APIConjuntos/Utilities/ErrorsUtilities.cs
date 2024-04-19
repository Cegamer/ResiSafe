using Microsoft.AspNetCore.Mvc;
using System.Net;

namespace APIConjuntos.Utilities
{
    public class ErrorsUtilities
    {
        public static ProblemDetails datosNulos = new ProblemDetails
        {
            Status = (int)HttpStatusCode.BadRequest,
            Title = "Los datos no pueden ser nulos",
            Detail = "No se ha ingresado ningún dato"
        };
        public static ProblemDetails sinAccesoAlRecurso = new ProblemDetails
        {
            Status = (int)HttpStatusCode.Unauthorized,
            Title = "No tiene acceso al recurso",
            Detail = "LOGIN-03",
        };

        public static ProblemDetails errorAlCrear = new ProblemDetails
        {
            Status = (int)HttpStatusCode.BadRequest,
            Title = "Error al crear",
            Detail = "Error al crear",
        };
    }
}

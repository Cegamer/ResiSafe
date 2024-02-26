using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class RegistroVigilante
    {
        public int IdRegistro { get; set; }
        public int IdVigilante { get; set; }
        public TimeSpan HoraEntrada { get; set; }
        public TimeSpan? HoraSalida { get; set; }
        public DateTime Fecha { get; set; }

        public virtual Perfil IdVigilanteNavigation { get; set; } = null!;
    }
}

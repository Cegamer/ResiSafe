using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class RegistroVisitante
    {
        public int IdRegistro { get; set; }
        public int IdVisitante { get; set; }
        public int IdResidenteVinculado { get; set; }
        public int IdVigilanteQueRegistra { get; set; }
        public DateTime Fecha { get; set; }
        public TimeSpan HoraIngreso { get; set; }
        public TimeSpan? HoraSalida { get; set; }

        public virtual Perfil IdResidenteVinculadoNavigation { get; set; } = null!;
        public virtual Perfil IdVigilanteQueRegistraNavigation { get; set; } = null!;
        public virtual Visitante IdVisitanteNavigation { get; set; } = null!;
    }
}

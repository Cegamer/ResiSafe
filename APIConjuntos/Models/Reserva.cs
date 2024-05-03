using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Reserva
    {
        public int IdReserva { get; set; }
        public int IdReservante { get; set; }
        public int IdZonaComun { get; set; }
        public DateTime Fecha { get; set; }
        public TimeSpan HoraInicio { get; set; }
        public TimeSpan HoraFin { get; set; }
        public int CantidadPersonas { get; set; }
        public int Estado { get; set; }

        public virtual Perfil IdReservanteNavigation { get; set; } = null!;
        public virtual Zonacomun IdZonaComunNavigation { get; set; } = null!;
    }
}

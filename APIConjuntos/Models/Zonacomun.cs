using System;
using System.Collections.Generic;

namespace APIConjuntos.Models
{
    public partial class Zonacomun
    {
        public Zonacomun()
        {
            Reservas = new HashSet<Reserva>();
        }

        public int IdZonaComun { get; set; }
        public int IdConjunto { get; set; }
        public string Nombre { get; set; } = null!;
        public TimeSpan HorarioApertura { get; set; }
        public TimeSpan HorarioCierre { get; set; }
        public int AforoMaximo { get; set; }
        public int Precio { get; set; }
        public int IdIcono { get; set; }
        /// <summary>
        /// En minutos
        /// </summary>
        public int IntervaloTurnos { get; set; }

        public virtual Conjunto IdConjuntoNavigation { get; set; } = null!;
        public virtual Icono IdIconoNavigation { get; set; } = null!;
        public virtual ICollection<Reserva> Reservas { get; set; }
    }
}

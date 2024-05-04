namespace APIConjuntos.DTO
{
    public class ReservasDTO
    {
        public int IdReserva { get; set; }
        public int IdReservante { get; set; }
        public int IdZonaComun { get; set; }
        public string Fecha { get; set; }
        public string HoraInicio { get; set; }
        public string HoraFin { get; set; }
        public int CantidadPersonas { get; set; }
        public int Estado { get; set; }
    }
}

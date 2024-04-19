namespace APIConjuntos.DTO
{
    public class ZonacomunDTO
    {
        public int IdZonaComun { get; set; }
        public int IdConjunto { get; set; }
        public string Nombre { get; set; } = null!;
        public string HorarioApertura { get; set; }
        public string HorarioCierre { get; set; }
        public int AforoMaximo { get; set; }
        public int Precio { get; set; }
        public int IdIcono { get; set; }
        public int IntervaloTurnos { get; set; }
    }
}

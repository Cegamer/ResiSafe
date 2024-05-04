namespace APIConjuntos.DTO
{
    public class PaqueteDTO
    {
        public int IdPaquete { get; set; }
        public string Torre { get; set; } = null!;
        public string? Apto { get; set; }
        public int IdVigilanteRecibe { get; set; }
        public int Estado { get; set; }
        public int IdResidenteRecibe { get; set; }
        public string FechaEntrega { get; set; }
        public string HoraEntrega { get; set; }
        public string? FechaRecibido { get; set; }
        public string? HoraRecibido { get; set; }
        public int IdConjunto { get; set; }
    }
}

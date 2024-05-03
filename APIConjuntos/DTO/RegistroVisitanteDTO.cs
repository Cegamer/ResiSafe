namespace APIConjuntos.DTO
{
    public class RegistroVisitanteDTO
    {
        public int IdRegistro { get; set; }
        public int IdVisitante { get; set; }
        public int IdResidenteVinculado { get; set; }
        public int IdVigilanteQueRegistra { get; set; }
        public string Fecha { get; set; }
        public string HoraIngreso { get; set; }
        public string? HoraSalida { get; set; }

    }
}

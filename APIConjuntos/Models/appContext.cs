using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace APIConjuntos.Models
{
    public partial class appContext : DbContext
    {
        public appContext()
        {
        }

        public appContext(DbContextOptions<appContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Conjunto> Conjuntos { get; set; } = null!;
        public virtual DbSet<Icono> Iconos { get; set; } = null!;
        public virtual DbSet<Paquete> Paquetes { get; set; } = null!;
        public virtual DbSet<Perfil> Perfils { get; set; } = null!;
        public virtual DbSet<RegistroVigilante> RegistroVigilantes { get; set; } = null!;
        public virtual DbSet<RegistroVisitante> RegistroVisitantes { get; set; } = null!;
        public virtual DbSet<Reserva> Reservas { get; set; } = null!;
        public virtual DbSet<Residente> Residentes { get; set; } = null!;
        public virtual DbSet<TiposDePerfil> TiposDePerfils { get; set; } = null!;
        public virtual DbSet<Usuario> Usuarios { get; set; } = null!;
        public virtual DbSet<Visitante> Visitantes { get; set; } = null!;
        public virtual DbSet<Zonacomun> Zonacomuns { get; set; } = null!;

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see http://go.microsoft.com/fwlink/?LinkId=723263.
                optionsBuilder.UseMySQL("Server=127.0.0.1;Port=3307;uid=root;pwd=root;Database=app");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Conjunto>(entity =>
            {
                entity.HasKey(e => e.IdConjunto)
                    .HasName("PRIMARY");

                entity.ToTable("conjunto");

                entity.Property(e => e.IdConjunto).HasColumnName("ID_CONJUNTO");

                entity.Property(e => e.Direccion).HasMaxLength(255);

                entity.Property(e => e.Nombre)
                    .HasMaxLength(255)
                    .HasColumnName("NOMBRE");
            });

            modelBuilder.Entity<Icono>(entity =>
            {
                entity.HasKey(e => e.IdIcono)
                    .HasName("PRIMARY");

                entity.ToTable("iconos");

                entity.Property(e => e.IdIcono).HasColumnName("ID_ICONO");

                entity.Property(e => e.Icono1).HasColumnName("ICONO");

                entity.Property(e => e.NombreIcono)
                    .HasMaxLength(255)
                    .HasColumnName("NOMBRE_ICONO");
            });

            modelBuilder.Entity<Paquete>(entity =>
            {
                entity.HasKey(e => e.IdPaquete)
                    .HasName("PRIMARY");

                entity.ToTable("paquete");

                entity.HasIndex(e => e.IdVigilanteRecibe, "paquete_ibfk_1");

                entity.HasIndex(e => e.IdResidenteRecibe, "paquete_ibfk_2");

                entity.Property(e => e.IdPaquete).HasColumnName("ID_PAQUETE");

                entity.Property(e => e.Apto)
                    .HasMaxLength(255)
                    .HasColumnName("APTO");

                entity.Property(e => e.Estado).HasColumnName("ESTADO");

                entity.Property(e => e.IdResidenteRecibe).HasColumnName("ID_RESIDENTE_RECIBE");

                entity.Property(e => e.IdVigilanteRecibe).HasColumnName("ID_VIGILANTE_RECIBE");

                entity.Property(e => e.Torre)
                    .HasMaxLength(255)
                    .HasColumnName("TORRE");

                entity.HasOne(d => d.IdResidenteRecibeNavigation)
                    .WithMany(p => p.PaqueteIdResidenteRecibeNavigations)
                    .HasForeignKey(d => d.IdResidenteRecibe)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("paquete_ibfk_2");

                entity.HasOne(d => d.IdVigilanteRecibeNavigation)
                    .WithMany(p => p.PaqueteIdVigilanteRecibeNavigations)
                    .HasForeignKey(d => d.IdVigilanteRecibe)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("paquete_ibfk_1");
            });

            modelBuilder.Entity<Perfil>(entity =>
            {
                entity.HasKey(e => e.IdPerfil)
                    .HasName("PRIMARY");

                entity.ToTable("perfil");

                entity.HasIndex(e => e.IdConjunto, "perfil_conjunto_idx");

                entity.HasIndex(e => e.IdTipoPerfil, "perfil_tipo_idx");

                entity.HasIndex(e => e.IdUsuario, "perfil_usuario_idx");

                entity.Property(e => e.IdPerfil).HasColumnName("ID_Perfil");

                entity.Property(e => e.IdConjunto).HasColumnName("ID_CONJUNTO");

                entity.Property(e => e.IdTipoPerfil).HasColumnName("ID_TIPO_PERFIL");

                entity.Property(e => e.IdUsuario).HasColumnName("ID_Usuario");

                entity.HasOne(d => d.IdConjuntoNavigation)
                    .WithMany(p => p.Perfils)
                    .HasForeignKey(d => d.IdConjunto)
                    .HasConstraintName("perfil_conjunto");

                entity.HasOne(d => d.IdTipoPerfilNavigation)
                    .WithMany(p => p.Perfils)
                    .HasForeignKey(d => d.IdTipoPerfil)
                    .HasConstraintName("perfil_tipo");

                entity.HasOne(d => d.IdUsuarioNavigation)
                    .WithMany(p => p.Perfils)
                    .HasForeignKey(d => d.IdUsuario)
                    .HasConstraintName("perfil_usuario");
            });

            modelBuilder.Entity<RegistroVigilante>(entity =>
            {
                entity.HasKey(e => e.IdRegistro)
                    .HasName("PRIMARY");

                entity.ToTable("registro_vigilantes");

                entity.HasIndex(e => e.IdVigilante, "registro_vigilantes_ibfk_1");

                entity.Property(e => e.IdRegistro).HasColumnName("ID_REGISTRO");

                entity.Property(e => e.Fecha)
                    .HasColumnType("date")
                    .HasColumnName("FECHA");

                entity.Property(e => e.HoraEntrada)
                    .HasColumnType("time")
                    .HasColumnName("HORA_ENTRADA");

                entity.Property(e => e.HoraSalida)
                    .HasColumnType("time")
                    .HasColumnName("HORA_SALIDA");

                entity.Property(e => e.IdVigilante).HasColumnName("ID_VIGILANTE");

                entity.HasOne(d => d.IdVigilanteNavigation)
                    .WithMany(p => p.RegistroVigilantes)
                    .HasForeignKey(d => d.IdVigilante)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("registro_vigilantes_ibfk_1");
            });

            modelBuilder.Entity<RegistroVisitante>(entity =>
            {
                entity.HasKey(e => e.IdRegistro)
                    .HasName("PRIMARY");

                entity.ToTable("registro_visitantes");

                entity.HasIndex(e => e.IdVisitante, "registro_visitantes");

                entity.HasIndex(e => e.IdResidenteVinculado, "registro_visitantes_ibfk_2");

                entity.HasIndex(e => e.IdVigilanteQueRegistra, "registro_visitantes_ibfk_3");

                entity.Property(e => e.IdRegistro).HasColumnName("ID_REGISTRO");

                entity.Property(e => e.Fecha)
                    .HasColumnType("date")
                    .HasColumnName("FECHA");

                entity.Property(e => e.HoraIngreso)
                    .HasColumnType("time")
                    .HasColumnName("HORA_INGRESO");

                entity.Property(e => e.HoraSalida)
                    .HasColumnType("time")
                    .HasColumnName("HORA_SALIDA");

                entity.Property(e => e.IdResidenteVinculado).HasColumnName("ID_RESIDENTE_VINCULADO");

                entity.Property(e => e.IdVigilanteQueRegistra).HasColumnName("ID_VIGILANTE_QUE_REGISTRA");

                entity.Property(e => e.IdVisitante).HasColumnName("ID_VISITANTE");

                entity.HasOne(d => d.IdResidenteVinculadoNavigation)
                    .WithMany(p => p.RegistroVisitanteIdResidenteVinculadoNavigations)
                    .HasForeignKey(d => d.IdResidenteVinculado)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("registro_visitantes_ibfk_2");

                entity.HasOne(d => d.IdVigilanteQueRegistraNavigation)
                    .WithMany(p => p.RegistroVisitanteIdVigilanteQueRegistraNavigations)
                    .HasForeignKey(d => d.IdVigilanteQueRegistra)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("registro_visitantes_ibfk_3");

                entity.HasOne(d => d.IdVisitanteNavigation)
                    .WithMany(p => p.RegistroVisitantes)
                    .HasForeignKey(d => d.IdVisitante)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("registro_visitantes");
            });

            modelBuilder.Entity<Reserva>(entity =>
            {
                entity.HasKey(e => e.IdReserva)
                    .HasName("PRIMARY");

                entity.ToTable("reserva");

                entity.HasIndex(e => e.IdReservante, "reserva_ibfk_1");

                entity.HasIndex(e => e.IdZonaComun, "reserva_ibfk_2");

                entity.Property(e => e.IdReserva).HasColumnName("ID_RESERVA");

                entity.Property(e => e.CantidadPersonas).HasColumnName("CANTIDAD_PERSONAS");

                entity.Property(e => e.Fecha)
                    .HasColumnType("date")
                    .HasColumnName("FECHA");

                entity.Property(e => e.HoraFin)
                    .HasColumnType("time")
                    .HasColumnName("HORA_FIN");

                entity.Property(e => e.HoraInicio)
                    .HasColumnType("time")
                    .HasColumnName("HORA_INICIO");

                entity.Property(e => e.IdReservante).HasColumnName("ID_RESERVANTE");

                entity.Property(e => e.IdZonaComun).HasColumnName("ID_ZONA_COMUN");

                entity.HasOne(d => d.IdReservanteNavigation)
                    .WithMany(p => p.Reservas)
                    .HasForeignKey(d => d.IdReservante)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("reserva_ibfk_1");

                entity.HasOne(d => d.IdZonaComunNavigation)
                    .WithMany(p => p.Reservas)
                    .HasForeignKey(d => d.IdZonaComun)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("reserva_ibfk_2");
            });

            modelBuilder.Entity<Residente>(entity =>
            {
                entity.HasKey(e => e.IdPerfil)
                    .HasName("PRIMARY");

                entity.ToTable("residentes");

                entity.Property(e => e.IdPerfil)
                    .ValueGeneratedOnAdd()
                    .HasColumnName("ID_Perfil");

                entity.Property(e => e.Apartamento)
                    .HasMaxLength(255)
                    .HasColumnName("APARTAMENTO");

                entity.Property(e => e.Torre)
                    .HasMaxLength(255)
                    .HasColumnName("TORRE");

                entity.HasOne(d => d.IdPerfilNavigation)
                    .WithOne(p => p.Residente)
                    .HasForeignKey<Residente>(d => d.IdPerfil)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("residentes_ibfk_1");
            });

            modelBuilder.Entity<TiposDePerfil>(entity =>
            {
                entity.HasKey(e => e.IdTipo)
                    .HasName("PRIMARY");

                entity.ToTable("tipos_de_perfil");

                entity.Property(e => e.IdTipo).HasColumnName("ID_TIPO");

                entity.Property(e => e.NombreTipo)
                    .HasMaxLength(45)
                    .HasColumnName("NOMBRE_TIPO");
            });

            modelBuilder.Entity<Usuario>(entity =>
            {
                entity.HasKey(e => e.IdUsuario)
                    .HasName("PRIMARY");

                entity.ToTable("usuario");

                entity.Property(e => e.IdUsuario).HasColumnName("ID_Usuario");

                entity.Property(e => e.Apellido)
                    .HasMaxLength(255)
                    .HasColumnName("APELLIDO");

                entity.Property(e => e.Cedula).HasColumnName("CEDULA");

                entity.Property(e => e.Contraseña).HasMaxLength(255);

                entity.Property(e => e.Foto).HasColumnName("FOTO");

                entity.Property(e => e.Nombre)
                    .HasMaxLength(255)
                    .HasColumnName("NOMBRE");
            });

            modelBuilder.Entity<Visitante>(entity =>
            {
                entity.HasKey(e => e.IdVisitante)
                    .HasName("PRIMARY");

                entity.ToTable("visitante");

                entity.Property(e => e.IdVisitante).HasColumnName("ID_VISITANTE");

                entity.Property(e => e.Apellido)
                    .HasMaxLength(255)
                    .HasColumnName("APELLIDO");

                entity.Property(e => e.Cedula).HasColumnName("CEDULA");

                entity.Property(e => e.Foto).HasColumnName("FOTO");

                entity.Property(e => e.Nombre)
                    .HasMaxLength(255)
                    .HasColumnName("NOMBRE");
            });

            modelBuilder.Entity<Zonacomun>(entity =>
            {
                entity.HasKey(e => e.IdZonaComun)
                    .HasName("PRIMARY");

                entity.ToTable("zonacomun");

                entity.HasIndex(e => e.IdConjunto, "zonacomun_ibfk_1");

                entity.HasIndex(e => e.IdIcono, "zonacomun_ibfk_2");

                entity.Property(e => e.IdZonaComun).HasColumnName("ID_ZONA_COMUN");

                entity.Property(e => e.AforoMaximo).HasColumnName("AFORO_MAXIMO");

                entity.Property(e => e.HorarioApertura)
                    .HasColumnType("time")
                    .HasColumnName("HORARIO_APERTURA");

                entity.Property(e => e.HorarioCierre)
                    .HasColumnType("time")
                    .HasColumnName("HORARIO_CIERRE");

                entity.Property(e => e.IdConjunto).HasColumnName("ID_CONJUNTO");

                entity.Property(e => e.IdIcono).HasColumnName("ID_ICONO");

                entity.Property(e => e.IntervaloTurnos)
                    .HasColumnName("INTERVALO_TURNOS")
                    .HasComment("En minutos");

                entity.Property(e => e.Nombre)
                    .HasMaxLength(255)
                    .HasColumnName("NOMBRE");

                entity.Property(e => e.Precio).HasColumnName("PRECIO");

                entity.HasOne(d => d.IdConjuntoNavigation)
                    .WithMany(p => p.Zonacomuns)
                    .HasForeignKey(d => d.IdConjunto)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("zonacomun_ibfk_1");

                entity.HasOne(d => d.IdIconoNavigation)
                    .WithMany(p => p.Zonacomuns)
                    .HasForeignKey(d => d.IdIcono)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("zonacomun_ibfk_2");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}

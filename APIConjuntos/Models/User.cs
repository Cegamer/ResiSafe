namespace APIConjuntos.Models
{
    public class User
    {

        public int id { get; set; }
        public int indentification { get; set; }
        public string name { get; set; }
        public string email { get; set; }
        public string password { get; set; }
        public int type { get; set; }

        public User(int id, int indentification, string name, string email, string password, int type)
        {
            this.id = id;
            this.indentification = indentification;
            this.name = name;
            this.email = email;
            this.password = password;
            this.type = type;
        }
    }
}

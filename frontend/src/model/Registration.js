export default class Registration {
    constructor() {
        this.password = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.profession = null;
        this.phoneNumber = null;
        this.workplace = {
            name: "",
            address: "",
            country: "",
            city: "",
            zip: ""
        }

        this.password = "123";
        this.firstName = "ime";
        this.lastName = "prezime";
        this.email = "noviautor@gmail.com";
        this.profession = "profesija";
        this.phoneNumber = "0621706259";
        this.workplace = {
            name: "workplace",
            address: "adr",
            country: "country",
            city: "city",
            zip: "21411"
        }
    }    
    reset() { 
        this.password = null;
        this.firstname = null;
        this.lastname = null;
        this.email = null;
        this.profession = null;
        this.phoneNumber = null;
        this.workplace = {
            name: "",
            address: "",
            country: "",
            city: "",
            zip: ""
        }
    }
}
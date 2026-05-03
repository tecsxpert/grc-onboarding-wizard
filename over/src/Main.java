class animal{
    void sound(){
        System.out.println("dog barks");
    }
    void sound(int a){
        System.out.println("tiger roars");
    }
}

class Main{
    void main(String[] args){
        animal a= new animal();
        a.sound();
        a.sound(1);


    }
}
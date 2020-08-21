package ec.edu.espe_innovativa.managed_beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "entregaCertificadoBean2")
@ViewScoped
public class EntregaCertificadoBean2 implements Serializable {
    
    private String nombres1;
    private String apellidos1;
    private String nombres2;
    private String apellidos2;
    private String nombres3;
    private String apellidos3;
    private Integer pantalla;
    
    public EntregaCertificadoBean2() {
    }

    @PostConstruct
    public void init() {
        pantalla=1;
    }
    public void aceptar1(){
        System.out.println("ok1");
        nombres2= "jose2";
        apellidos2 = "acevedo2";
        pantalla=2;
    }
    public void aceptar11(){
        System.out.println("ok11");
        nombres2= "jose21";
        apellidos2 = "acevedo21";
        pantalla=2;
    }
    public void aceptar2(){
        System.out.println("ok2");
        nombres3= "jose3";
        apellidos3 = "acevedo3";
        pantalla=3;
    }
    public void aceptar3(){
        System.out.println("ok3");
    }
    public void cancelar3(){
        pantalla=2;
    }
    public void cancelar2(){
        pantalla=1;
    }
    
    
    public String getNombres1() {
        return nombres1;
    }

    public void setNombres1(String nombres1) {
        this.nombres1 = nombres1;
    }

    public String getApellidos1() {
        return apellidos1;
    }

    public void setApellidos1(String apellidos1) {
        this.apellidos1 = apellidos1;
    }

    public Integer getPantalla() {
        return pantalla;
    }

    public void setPantalla(Integer pantalla) {
        this.pantalla = pantalla;
    }

    public String getNombres2() {
        return nombres2;
    }

    public void setNombres2(String nombres2) {
        this.nombres2 = nombres2;
    }

    public String getApellidos2() {
        return apellidos2;
    }

    public void setApellidos2(String apellidos2) {
        this.apellidos2 = apellidos2;
    }

    public String getNombres3() {
        return nombres3;
    }

    public void setNombres3(String nombres3) {
        this.nombres3 = nombres3;
    }

    public String getApellidos3() {
        return apellidos3;
    }

    public void setApellidos3(String apellidos3) {
        this.apellidos3 = apellidos3;
    }
    
    
    
}

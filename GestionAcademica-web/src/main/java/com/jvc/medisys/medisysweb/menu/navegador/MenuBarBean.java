package com.jvc.medisys.medisysweb.menu.navegador;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import static com.jvc.medisys.icefacesUtil.FacesUtils.getServletRequest;
import ec.edu.espe_innovativa.entity_bean.Opcion;
import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author Carlos
 */
@Named("menuBar")
@SessionScoped
public class MenuBarBean implements Serializable {
     private MenuModel menuModel;
    private Usuario usuario;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    private Integer opcion;

    private String usuarioBean;

    public MenuBarBean() {
    }

    public void inicializar(String usuarioBean) {
        this.usuarioBean = usuarioBean;
        usuario = usuarioFacadeLocal.findByIdentificacion(this.usuarioBean);
        this.menuBean();
    }

    @PostConstruct
    private void inicializarBean() {
        try {
            this.usuarioBean = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
            inicializar(usuarioBean);

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(String usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    /**
     * Obtiene la lista de opciones del menÃº principal y genera el menÃº
     * correspondiente para el usuarioBean y su perfil.
     */
    private void menuBean() {
        this.menuModel = new DefaultMenuModel();
        try {
            List<Opcion> opcionesNivelCero = new ArrayList<>();
            for (Rol p : usuario.getRolesList()) {
                opcionesNivelCero.addAll(p.getOpcionList().stream()
                        .map(op -> op.getOpcionNivelCero())
                        .filter(op -> op.getEstado()=='A')
                        .distinct()
                        .collect(Collectors.toList()));
            }

            opcionesNivelCero = opcionesNivelCero.stream().distinct().sorted().collect(Collectors.toList());

            for (Opcion opcion : opcionesNivelCero) {
                DefaultSubMenu submenu = new DefaultSubMenu();
                submenu.setLabel(opcion.getNombre());
                //submenu.setStyle("width:190px");
                this.populateMenu(opcion, submenu);
                this.menuModel.addElement(submenu);

            }
            
            /*DefaultSubMenu submenu = new DefaultSubMenu();
            submenu.setLabel("Usuario: " + usuario.getPersona().getNombresCompletos());
            submenu.setStyle("float:right");
            menuModel.addElement(submenu);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera de manera recursiva las opciones del sub-menÃº.
     *
     * @param opcionPadre Opciones del menÃº padre.
     * @param menuPadre Componente menÃº.
     */
    public void populateMenu(Opcion opcionPadre, DefaultSubMenu menuPadre) {
        try {
            List<Opcion> opcionesHijas = new ArrayList<>();

            for (Rol p : usuario.getRolesList()) {
                opcionesHijas.addAll(p.getOpcionList().stream()
                        .filter(op -> op.esOpcionPadre(opcionPadre) && op.getEstado()=='A')
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList()));
            }
            opcionesHijas = opcionesHijas.stream().sorted().distinct().collect(Collectors.toList());

            long nroHijos = 0;
            for (Opcion opcion : opcionesHijas) {
                for (Rol p : usuario.getRolesList()) {
                    nroHijos = p.getOpcionList().stream()
                            .filter(op -> op.esOpcionPadre(opcion) && op.getEstado()=='A')
                            .count();
                    if (nroHijos > 0) {
                        break;
                    }
                }

                if (nroHijos > 0) {
                    DefaultSubMenu itemHijo = new DefaultSubMenu();
                    itemHijo.setLabel(opcion.getNombre());
                    //itemHijo.setStyle("width:190px");
                    this.populateMenu(opcion, itemHijo);
                    menuPadre.getElements().add(itemHijo);
                } else {
                    DefaultMenuItem itemHijo = new DefaultMenuItem();
                    itemHijo.setValue(opcion.getNombre());
                    itemHijo.setUrl(getServletRequest().getContextPath() + opcion.getUrl());
                    //itemHijo.setStyle("width:190px");
                    itemHijo.setImmediate(true);
                    menuPadre.getElements().add(itemHijo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.write("Error en generacion de menu: " + e.getStackTrace().toString(), Log.nivelFatal);
        }
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public Integer getOpcion() {
        return opcion;
    }

    public void setOpcion(Integer opcion) {
        this.opcion = opcion;
    }
    
    public void recargarUsuario(){
        usuario = usuarioFacadeLocal.find(usuario.getId());
    }
    
}

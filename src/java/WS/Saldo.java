/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Choringa
 */
@Entity
@Table(name = "saldo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Saldo.findAll", query = "SELECT s FROM Saldo s"),
    @NamedQuery(name = "Saldo.findByIdSaldo", query = "SELECT s FROM Saldo s WHERE s.idSaldo = :idSaldo"),
    @NamedQuery(name = "Saldo.findBySaldo", query = "SELECT s FROM Saldo s WHERE s.saldo = :saldo"),
    @NamedQuery(name = "Saldo.findByVencimiento", query = "SELECT s FROM Saldo s WHERE s.vencimiento = :vencimiento")})
public class Saldo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_saldo")
    private Integer idSaldo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "saldo")
    private String saldo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vencimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vencimiento;
    @JoinColumn(name = "usuario_asociado", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuarios usuarioAsociado;

    public Saldo() {
    }

    public Saldo(Integer idSaldo) {
        this.idSaldo = idSaldo;
    }

    public Saldo(Integer idSaldo, String saldo, Date vencimiento) {
        this.idSaldo = idSaldo;
        this.saldo = saldo;
        this.vencimiento = vencimiento;
    }

    public Integer getIdSaldo() {
        return idSaldo;
    }

    public void setIdSaldo(Integer idSaldo) {
        this.idSaldo = idSaldo;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public Usuarios getUsuarioAsociado() {
        return usuarioAsociado;
    }

    public void setUsuarioAsociado(Usuarios usuarioAsociado) {
        this.usuarioAsociado = usuarioAsociado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSaldo != null ? idSaldo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Saldo)) {
            return false;
        }
        Saldo other = (Saldo) object;
        if ((this.idSaldo == null && other.idSaldo != null) || (this.idSaldo != null && !this.idSaldo.equals(other.idSaldo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WS.Saldo[ idSaldo=" + idSaldo + " ]";
    }
    
}

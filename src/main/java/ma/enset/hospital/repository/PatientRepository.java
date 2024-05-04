package ma.enset.hospital.repository;

import ma.enset.hospital.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    // quand vous avez une méthode qui retourne une page il est obligatoire d'ajouter un deuxième paramètre de type Pageable
    Page<Patient> findByNomContains(String keyword, Pageable pageable);//pageable il sert pour fair la pagination => je peux transmettre le numéro de la page et size

    //Method 2
    @Query("select p from Patient p where p.nom like :x")
    Page<Patient> chercher(@Param("x") String keyword, Pageable pageable);
}

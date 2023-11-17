package de.ait.gp.repositories;

import de.ait.gp.models.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesInfoRepository extends JpaRepository<FileInfo, Long> {
}

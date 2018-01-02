
package com.shj.security.rbac.repository;

import com.shj.security.rbac.domain.Resource;
import org.springframework.stereotype.Repository;


/**
 * @author zhailiang
 *
 */
@Repository
public interface ResourceRepository extends ImoocRepository<Resource> {

	Resource findByName(String name);

}

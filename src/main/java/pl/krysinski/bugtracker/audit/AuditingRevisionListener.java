package pl.krysinski.bugtracker.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.krysinski.bugtracker.security.SecurityService;

@Component
public class AuditingRevisionListener implements RevisionListener {

    private final SecurityService securityService;

    @Autowired
    public AuditingRevisionListener(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void newRevision(Object revisionEntity) {
        AuditedRevisionEntity auditedRevisionEntity = (AuditedRevisionEntity) revisionEntity;

        String actor = securityService.getUsernameLoggedUser();

        auditedRevisionEntity.setActor(actor);
    }
}

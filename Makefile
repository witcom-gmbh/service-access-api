MIGRATION_LABEL = "to-be-changed"
DATE_WITH_TIME := $(shell /bin/date "+%Y%m%d%H%M%S")

makeMigration:
	mvn compile liquibase:diff -DdiffChangeLogFile=src/main/resources/db/changelog/changes/${DATE_WITH_TIME}-${MIGRATION_LABEL}.yaml
	@echo "  - include:" >> src/main/resources/db/changelog/db.changelog-master.yaml
	@echo "      file: db/changelog/changes/$(DATE_WITH_TIME)-$(MIGRATION_LABEL).yaml" >> src/main/resources/db/changelog/db.changelog-master.yaml

update:
	mvn compile liquibase:update

clean:
	rm -f target/serviceaccess-dev*
	rm -f src/main/resources/db/changelog/changes/*-*.yaml
	sed  -i '/#MARKER#/q' src/main/resources/db/changelog/db.changelog-master.yaml


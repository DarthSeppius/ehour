package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.service.backup.BackupEntityLocator;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import org.springframework.util.Assert;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 12/6/10 11:59 PM
 */
public class XmlParserBuilder {
    private XMLEventReader xmlReader;
    private ConfigurationDao configurationDao;
    private ConfigurationParserDao configurationParserDao;
    private DomainObjectParserDao domainObjectParserDao;
    private UserRoleParserDao userRoleParserDao;
    private boolean skipValidation = false;
    private BackupEntityLocator backupEntityLocator;

    public XmlParser build() throws XMLStreamException {
        Assert.notNull(xmlReader);
        Assert.notNull(configurationDao);
        Assert.notNull(domainObjectParserDao);
        Assert.notNull(userRoleParserDao);
        Assert.notNull(backupEntityLocator);

        PrimaryKeyCache keyCache = new PrimaryKeyCache();

        JoinTableParser joinTableParser = new JoinTableParser(xmlReader, backupEntityLocator);

        DomainObjectParser parser = new DomainObjectParser(xmlReader, domainObjectParserDao, keyCache, backupEntityLocator);
        ConfigurationParser configurationParser = new ConfigurationParser(configurationParserDao);
        UserRoleParser userRoleParser = new UserRoleParser(userRoleParserDao, keyCache, backupEntityLocator.userRoleBackupEntity());

        return new XmlParser(configurationDao, parser, configurationParser, joinTableParser, userRoleParser, skipValidation);
    }

    public XmlParserBuilder setSkipValidation(boolean skipValidation) {
        this.skipValidation = skipValidation;
        return this;
    }

    public XmlParserBuilder setXmlReader(XMLEventReader xmlReader) {
        this.xmlReader = xmlReader;
        return this;
    }

    public XmlParserBuilder setConfigurationDao(ConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
        return this;
    }

    public XmlParserBuilder setConfigurationParserDao(ConfigurationParserDao configurationParserDao) {
        this.configurationParserDao = configurationParserDao;
        return this;
    }

    public XmlParserBuilder setDomainObjectParserDao(DomainObjectParserDao domainObjectParserDao) {
        this.domainObjectParserDao = domainObjectParserDao;
        return this;
    }

    public XmlParserBuilder setUserRoleParserDao(UserRoleParserDao userRoleParserDao) {
        this.userRoleParserDao = userRoleParserDao;
        return this;
    }

    public XmlParserBuilder setBackupEntityLocator(BackupEntityLocator backupEntityLocator) {
        this.backupEntityLocator = backupEntityLocator;
        return this;
    }
}

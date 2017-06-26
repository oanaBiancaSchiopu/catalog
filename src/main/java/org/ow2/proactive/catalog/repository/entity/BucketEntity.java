/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.ow2.proactive.catalog.repository.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


/**
 * @author ActiveEon Team
 */
@AllArgsConstructor
@Data
@Entity
@Table(name = "BUCKET", uniqueConstraints = @UniqueConstraint(columnNames = { "NAME", "OWNER" }))
@ToString(exclude = "catalogObjects")
public class BucketEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    protected Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "OWNER", nullable = false)
    protected String owner;

    @OneToMany(mappedBy = "bucket", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
                                                                        CascadeType.REMOVE }, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 10)
    private Set<CatalogObjectEntity> catalogObjects = new HashSet<>();

    public BucketEntity() {
        catalogObjects = new HashSet<>();
    }

    public BucketEntity(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.catalogObjects = new HashSet<>();
    }

    public void addCatalogObject(CatalogObjectEntity catalogObject) {
        this.catalogObjects.add(catalogObject);
        catalogObject.setBucket(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        BucketEntity that = (BucketEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}

/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * ModeShape is free software. Unless otherwise indicated, all code in ModeShape
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * ModeShape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.jboss.subsystem;

import javax.jcr.RepositoryException;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import org.jboss.dmr.ModelNode;
import org.modeshape.jboss.service.RepositoryService;

/**
 * An {@link org.jboss.as.controller.OperationStepHandler} implementation that handles changes to the model values for
 * a text extractor submodel's {@link org.jboss.as.controller.AttributeDefinition attribute definitions}.
 * Those attributes that can be changed {@link org.jboss.as.controller.registry.AttributeAccess.Flag#RESTART_NONE RESTART_NONE without restarting}
 * will be immediately reflected in the repository's configuration; other attributes will be changed in the submodel and used upon the next restart.
 */
public class TextExtractorWriteAttributeHandler extends AbstractRepositoryConfigWriteAttributeHandler {

    static final TextExtractorWriteAttributeHandler INSTANCE = new TextExtractorWriteAttributeHandler();

    private TextExtractorWriteAttributeHandler() {
        super(ModelAttributes.TEXT_EXTRACTOR_ATTRIBUTES);
    }

    @Override
    protected boolean changeField( OperationContext context,
                                   ModelNode operation,
                                   RepositoryService repositoryService,
                                   MappedAttributeDefinition defn,
                                   ModelNode newValue ) throws RepositoryException, OperationFailedException {
        String extractorName = extractorName(operation);
        repositoryService.changeTextExtractorField(defn, newValue, extractorName);
        return true;
    }

    protected final String extractorName( ModelNode operation ) {
        PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR));
        return address.getLastElement().getValue();
    }

}

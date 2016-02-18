%{ 
 ********************************************************************************************
 Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.

 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse Public License v1.0
 and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 
 The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 and the Eclipse Distribution License is available at
 http://www.eclipse.org/org/documents/edl-v10.php.
   
 Contributors:
   
 Axel Reichwein (axel.reichwein@koneksys.com)		- initial implementation       
 ********************************************************************************************
%}
function addSimulinkBlock3(modelName, blockType, blockQualifiedName)
    try       
        add_block(strcat('built-in/', blockType),blockQualifiedName);       
    catch err             
        %open file
        fid = fopen('logFile','a+');
        % write the error to file
        % first line: message
        fprintf(fid,'%s\n',strcat(datestr(now),' --- ',err.message));

        % close file
        fclose(fid);
      
    end
end


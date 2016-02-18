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

function openSimulinkModel3(modelName)
    try
    cd matlab;   
    % check if Simulink model exists
    % load all simulink models in workspace
    simulinkModelList = ls('*.slx');
    [modelCount, modelNameLength] = size(simulinkModelList);
    for i = 1 : modelCount    
     % load Simulink model
        load_system(strtrim(simulinkModelList(i,:)));      
    end

    % only works if simulink models are loaded in memory
    blockDiagrams_cell = find_system('type', 'block_diagram');

    simulinkModelExists = false;
    for i = 1 : length(blockDiagrams_cell)
        if(strcmp(blockDiagrams_cell(i),modelName))
            simulinkModelExists = true;
            modelHandle = blockDiagrams_cell(i);
            break;
        end
    end
    % create Simulink model

    if(~simulinkModelExists)
        modelHandle = new_system(modelName);
        open_system(modelHandle);
    end

    
    catch err
        %rethrow(err);
        
        %open file
        fid = fopen('logFile','a+');
        
        % write the error to file
        % first line: message
        fprintf(fid,'%s\n',strcat(datestr(now),' --- ',err.message));

        % close file
        fclose(fid);
       
      
    end
end
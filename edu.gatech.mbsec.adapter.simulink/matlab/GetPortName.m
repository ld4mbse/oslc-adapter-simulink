% given a port handle, get the portname with compatibility considerations
function [PortName,PortType] = GetPortName(portHandle)

    % get name of the parent block
    ParentStr = get(portHandle,'Parent');
    
    % get handle of the parent block
    ParentHandle = get_param(ParentStr,'Handle');
    
    % get port handles structure
    portHandlesStruct = get_param(ParentHandle,'PortHandles');
    
    % get field names for the port handles structure
    PortHandlesNames = fieldnames(portHandlesStruct);
    
    % go through port handle names and find the correct one for this port
    for j = 1:length(PortHandlesNames)
        [LIA,~] = ismember(portHandle,portHandlesStruct.(PortHandlesNames{j}));
        if LIA % if the current field contains the port handle
            PortConName = PortHandlesNames{j}; % record the port connection name 
        end
    end

    % create custom names
    switch PortConName
        case 'Inport'
            PortName = [get(portHandle,'Parent'),'/',...
                'inport/',num2str(get(portHandle,'PortNumber'))];
            PortType = 'inputPort';
        case 'Outport'
            PortName = [get(portHandle,'Parent'),'/',...
                'outport/',num2str(get(portHandle,'PortNumber'))];
            PortType = 'outputPort';
        otherwise 
            PortName = [get(portHandle,'Parent'),'/',...
                PortConName,num2str(get(portHandle,'PortNumber'))];  
            PortType = 'inputPort';
    end
    
    % modify the string for compatibility
    PortName = regexprep(PortName,'\s+',' '); % remove the double space if present
    PortName = strrep(PortName, ' ', '_'); % IDs in XMI cannot contain white-space characters
    PortName = strrep(PortName,sprintf('\n'),'_'); % IDs in XMI cannot contain newline characters
end
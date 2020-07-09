package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
PlaceProperty


*/

public abstract class PlacePropertyBase extends ReferenceableProperty {

	
	public static class IsInitial  {protected void copy(IsInitial instance) {}protected void merge(IsInitial child) {}}protected IsInitial _isInitial;public boolean isInitial() { return (_isInitial != null); }public IsInitial getIsInitial() { return _isInitial; }public void setIsInitial(boolean value) { if(value) _isInitial = new IsInitial(); else {_isInitial = null;}}public static class IsOust  {protected void copy(IsOust instance) {}protected void merge(IsOust child) {}}protected IsOust _isOust;public boolean isOust() { return (_isOust != null); }public IsOust getIsOust() { return _isOust; }public void setIsOust(boolean value) { if(value) _isOust = new IsOust(); else {_isOust = null;}}public static class IsFinal  {protected void copy(IsFinal instance) {}protected void merge(IsFinal child) {}}protected IsFinal _isFinal;public boolean isFinal() { return (_isFinal != null); }public IsFinal getIsFinal() { return _isFinal; }public void setIsFinal(boolean value) { if(value) _isFinal = new IsFinal(); else {_isFinal = null;}}public static class BackEnableProperty  {protected void copy(BackEnableProperty instance) {}protected void merge(BackEnableProperty child) {}}protected BackEnableProperty _backEnableProperty;public BackEnableProperty getBackEnable() { return _backEnableProperty; }public void setBackEnable(BackEnableProperty value) { if(_backEnableProperty!=null) _backEnableProperty.merge(value); else {_backEnableProperty = value;} }
	protected EditionActionProperty _editionActionProperty;protected WaitActionProperty _waitActionProperty;protected SendResponseActionProperty _sendResponseActionProperty;protected SendRequestActionProperty _sendRequestActionProperty;protected SendJobActionProperty _sendJobActionProperty;protected LineActionProperty _lineActionProperty;protected EnrollActionProperty _enrollActionProperty;protected DoorActionProperty _doorActionProperty;protected DelegationActionProperty _delegationActionProperty;public EditionActionProperty getEditionActionProperty() { return _editionActionProperty; }public void setEditionActionProperty(EditionActionProperty value) { if (_editionActionProperty == null) _editionActionProperty = value; else {_editionActionProperty.merge(value);} }public WaitActionProperty getWaitActionProperty() { return _waitActionProperty; }public void setWaitActionProperty(WaitActionProperty value) { if (_waitActionProperty == null) _waitActionProperty = value; else {_waitActionProperty.merge(value);} }public SendResponseActionProperty getSendResponseActionProperty() { return _sendResponseActionProperty; }public void setSendResponseActionProperty(SendResponseActionProperty value) { if (_sendResponseActionProperty == null) _sendResponseActionProperty = value; else {_sendResponseActionProperty.merge(value);} }public SendRequestActionProperty getSendRequestActionProperty() { return _sendRequestActionProperty; }public void setSendRequestActionProperty(SendRequestActionProperty value) { if (_sendRequestActionProperty == null) _sendRequestActionProperty = value; else {_sendRequestActionProperty.merge(value);} }public SendJobActionProperty getSendJobActionProperty() { return _sendJobActionProperty; }public void setSendJobActionProperty(SendJobActionProperty value) { if (_sendJobActionProperty == null) _sendJobActionProperty = value; else {_sendJobActionProperty.merge(value);} }public LineActionProperty getLineActionProperty() { return _lineActionProperty; }public void setLineActionProperty(LineActionProperty value) { if (_lineActionProperty == null) _lineActionProperty = value; else {_lineActionProperty.merge(value);} }public EnrollActionProperty getEnrollActionProperty() { return _enrollActionProperty; }public void setEnrollActionProperty(EnrollActionProperty value) { if (_enrollActionProperty == null) _enrollActionProperty = value; else {_enrollActionProperty.merge(value);} }public DoorActionProperty getDoorActionProperty() { return _doorActionProperty; }public void setDoorActionProperty(DoorActionProperty value) { if (_doorActionProperty == null) _doorActionProperty = value; else {_doorActionProperty.merge(value);} }public DelegationActionProperty getDelegationActionProperty() { return _delegationActionProperty; }public void setDelegationActionProperty(DelegationActionProperty value) { if (_delegationActionProperty == null) _delegationActionProperty = value; else {_delegationActionProperty.merge(value);} }

	public void copy(PlacePropertyBase instance) {
		this._code = instance._code;
this._name = instance._name;

		this._isInitial = instance._isInitial; 
this._isOust = instance._isOust; 
this._isFinal = instance._isFinal; 
this._backEnableProperty = instance._backEnableProperty; 

		this.setEditionActionProperty(instance._editionActionProperty);this.setWaitActionProperty(instance._waitActionProperty);this.setSendResponseActionProperty(instance._sendResponseActionProperty);this.setSendRequestActionProperty(instance._sendRequestActionProperty);this.setSendJobActionProperty(instance._sendJobActionProperty);this.setLineActionProperty(instance._lineActionProperty);this.setEnrollActionProperty(instance._enrollActionProperty);this.setDoorActionProperty(instance._doorActionProperty);this.setDelegationActionProperty(instance._delegationActionProperty);
	}

	public void merge(PlacePropertyBase child) {
		super.merge(child);
		
		
		if(_isInitial == null) _isInitial = child._isInitial; else if (child._isInitial != null) {_isInitial.merge(child._isInitial);}
if(_isOust == null) _isOust = child._isOust; else if (child._isOust != null) {_isOust.merge(child._isOust);}
if(_isFinal == null) _isFinal = child._isFinal; else if (child._isFinal != null) {_isFinal.merge(child._isFinal);}
if(_backEnableProperty == null) _backEnableProperty = child._backEnableProperty; else if (child._backEnableProperty != null) {_backEnableProperty.merge(child._backEnableProperty);}

		this.setEditionActionProperty(child._editionActionProperty);this.setWaitActionProperty(child._waitActionProperty);this.setSendResponseActionProperty(child._sendResponseActionProperty);this.setSendRequestActionProperty(child._sendRequestActionProperty);this.setSendJobActionProperty(child._sendJobActionProperty);this.setLineActionProperty(child._lineActionProperty);this.setEnrollActionProperty(child._enrollActionProperty);this.setDoorActionProperty(child._doorActionProperty);this.setDelegationActionProperty(child._delegationActionProperty);
	}

	public Class<?> getMetamodelClass() {
		return PlacePropertyBase.class;
	}

}


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<c:if test="${viewType eq 'grid'}">
<ul class='grid animal'>
	<c:if test='${empty list.item}'>
	<li style='width:100%; height:39px; padding-top:6px;'>해당 유기동물이 없습니다.</li>
	</c:if>
	<c:forEach items="${list.item}" var="vo" varStatus="status">
	<li ${status.index lt 5 ? 'class="first-row"' : ''} 
		data-filename='${vo.filename}'  data-age='${vo.age}' data-sexcd='${vo.sexCd}' 
		data-weight='${vo.weight}' data-colorcd='${vo.colorCd}' data-happendt='${vo.happenDt}'
		data-specialmark='${vo.specialMark}' data-happenplace='${vo.happenPlace}'
		data-processstate='${vo.processState}'
		data-carenm='${vo.careNm}' data-careaddr='${vo.careAddr}' data-caretel='${vo.careTel}'
	>
		<div class='li-top'>
			<div><img src='${vo.filename}'/></div>
			<div class='spec'>
				<div><span>${vo.age}</span>
					<span style='float:right;'>${vo.sexCd}</span>
				</div>
				<div>${vo.weight}</div>
				<div>${vo.colorCd}</div>
				<div>${vo.processState}</div>
			</div>
		</div>
		<div><span>${vo.careNm}</span>
			<span style='float:right;'>${vo.happenDt}</span>
		</div>
	</li>
	</c:forEach>
</ul>
</c:if>

<c:if test="${viewType eq 'list'}">
<table class='tb_list animal' >
	<c:if test="${empty list.item}">
	<thead>
		<tr><td>해당 유기동물이 없습니다.</td></tr>
	</thead>
	</c:if>
	<tbody>
		<c:forEach items="${list.item}" var="vo">
		<tr><td class='w-px100' rowspan='3'><img src='${vo.filename}'/></td>
			<th class='w-px80'>성별</th><td class='w-px60'>${vo.sexCd}</td>
			<th class='w-px40'>나이</th><td class='w-px100'>${vo.age}</td>
			<th class='w-px40'>체중</th><td class='w-px80'>${vo.weight}</td>
			<th class='w-px40'>색상</th><td>${vo.colorCd}</td>
			<th class='w-px60'>접수일자</th><td class='w-px100'>${vo.happenDt}</td>
		</tr>
		<tr><th>특징</th>
			<td colspan='9' class='left'>${vo.specialMark}</td>
		</tr>
		<tr><th>발견장소</th>
			<td colspan='8' class='left'>${vo.happenPlace}</td>
			<td>${vo.processState}</td>
		</tr>
		<tr><td colspan='2'>${vo.careNm}</td>
			<td colspan='7' class='left'>${vo.careAddr}</td>
			<td colspan='2'>${vo.careTel}</td>
		</tr>
		</c:forEach>
	</tbody>
</table>    
</c:if>

<input type='hidden' id='count' value='${list.count}'/>




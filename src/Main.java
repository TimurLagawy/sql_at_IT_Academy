public class Main {
    public static void main(String[] args) {
/*
Вам необходимо написать SQL запросы, перечисленные ниже.

Data base Sales History

1. Вывести все строки и столбцы таблицы Products, где в названии продукта присутствует "CD-ROM"
select *
from sh.products
where prod_name like '%CD-ROM%'

2. Выбрать из таблицы Customers, имя и фамилию заказчика, не повторяя их, которым на текущий момент больше 65 лет.

select distinct(cust_first_name||' '||cust_last_name) as cust_full_name, extract(year from sysdate)-cust_year_of_birth as cust_age
from sh.customers
where extract(year from sysdate)-cust_year_of_birth>65
order by cust_age

3. Выбрать из таблицы Sales заказы, совершенные 31 декабря 2001 года. вывести все столбцы таблицы.

select distinct(cust_first_name||' '||cust_last_name) as cust_full_name, extract(year from sysdate)-cust_year_of_birth as cust_age
from sh.customers
where extract(year from sysdate)-cust_year_of_birth>65
order by cust_age

4. Посчитать сумму всех заказов, которые были оформлены 1 января 1998 года.

select SUM(amount_sold)
from sh.sales
having time_id='01-JAN-98'
group by time_id

5. Вывести итоговую сумму всех заказов для каждого заказчика из таблицы Sales.

select c.cust_first_name||' '||c.cust_last_name as cust_full_name, sum(s.amount_sold)
from sh.sales s
join sh.customers c on s.cust_id=c.cust_id
group by c.cust_first_name||' '||c.cust_last_name

6. Если сумма всех заказов заказчика меньше 1000, то присвоить характеристику заказчику "not important", если от 1000 до до 50000 - "importanсe - low", если от 50000 до 100000 - "importance - high", во всех остальных случаях - "importance - highest"

select cust_id, sum(amount_sold),
 CASE
    when sum(amount_sold) < 1000 then 'not important'
    when sum(amount_sold) between 1000 and 50000 then 'importance-low'
    when sum(amount_sold) between 50000 and 100000 then 'importance-high'
    else 'importance-highest'
 END AS type
FROM sh.sales
group by cust_id
order by cust_id

7. Вывести полное имя заказчика, сумму его минимального заказа, максимального заказа и количество заказов в целом.

select c.cust_first_name||' '||c.cust_last_name as cust_full_name, min(s.amount_sold), max(s.amount_sold), sum(s.quantity_sold)
from sh.sales s
join sh.customers c on s.cust_id=c.cust_id
group by c.cust_first_name||' '||c.cust_last_name

8. Вывести название продукта, разница между ценой продажи и ценой себестоимости продукта максимальная и минимальная.

select p.prod_name, max(c.unit_price-c.unit_cost), min(c.unit_price-c.unit_cost)
from sh.products p
left join sh.costs c on p.prod_id=c.prod_id
group by p.prod_name

9. Вывести имя товара, стоимость товара за одну единицу и количество продаж этого товара.

select p.prod_name, c.unit_price, count(l.quantity_sold)
from sh.products p
join sh.costs c on p.prod_id=c.prod_id
join sh.sales l on p.prod_id=l.prod_id
group by p.prod_name, c.unit_price

10. Вывести все название товара, стоимость за одну единицу и  фамилии всех людей (отсортированные по алфавиту), которые купили этот товар. При этом название товара не должно повторяться.

select distinct(p.prod_name), c.unit_price, s.cust_last_name
from sh.products p
left join sh.costs c on p.prod_id=c.prod_id
join sh.sales l on p.prod_id=l.prod_id
join sh.customers s on l.cust_id=s.cust_id
group by p.prod_name, c.unit_price, s.cust_last_name
order by s.cust_last_name asc

11. Вывести заказчиков-мужчин, имя которых начинается на букву А и всех заказчиков-мужчин из Oran, отсортировать в обратном порядке по возрасту.

select cust_first_name, cust_city, extract(year from sysdate)-cust_year_of_birth as cust_age
from sh.customers
where cust_gender='M'
and cust_first_name like 'A%'
and cust_city='Oran'
order by cust_age desc

Data base Human Resources
12. Вывести сотрудников, у кого нет job history.

select e.last_name||' '||e.first_name as full_name, j.department_id
from hr.employees e
left join hr.job_history j
on e.employee_id=j.employee_id
where j.department_id is null

13. Вывести имена отделов, где все сотрудники работают на разных должностях.

select d.department_name, e.job_id
from hr.departments d
left join hr.employees e on d.department_id=e.department_id
having count(e.job_id) = 1
group by d.department_name, e.job_id
order by d.department_name

14. Посчитать количество работников, нанятых каждый день в 2003 году.

select count(employee_id), hire_date
from hr.employees
having extract(year from hire_date)=2003
group by hire_date

15. Посчитать общую сумму зарплаты вместе с бонусом для каждого отдела, отсортировать по убыванию.

select department_id, sum(salary+salary*nvl(commission_pct,0)) as sum_salary
from hr.employees
group by department_id
order by sum_salary desc

16. Вывести полное имя сотрудника и полное имя соответствующего ему менеджера.

select e.first_name||' '||e.last_name as emp_full_name, l.first_name||' '||l.last_name as man_full_name
from hr.employees e
left join hr.employees l
on e.manager_id=l.employee_id
order by man_full_name

17. Вывести имя и фамилию каждого сотрудника, кто у кого будет юбилей работы в 2024 году. Посчитать, сколько часов проработал каждый из этих сотрудников на момент 31-декабря-2024 года.

select first_name, last_name, trunc((to_date('31-DEC-24','DD-MM-YY')-hire_date)/365.25) as years_of_work, trunc((to_date('31-DEC-24','DD-MM-YY')-hire_date))*24 as hour_of_work
from hr.employees
where trunc((to_date('31-DEC-24','DD-MM-YY')-hire_date)/365.25)=5
or trunc((to_date('31-DEC-24','DD-MM-YY')-hire_date)/365.25)=10
or trunc((to_date('31-DEC-24','DD-MM-YY')-hire_date)/365.25)=15
or trunc((to_date('31-DEC-24','DD-MM-YY')-hire_date)/365.25)=20
or trunc((to_date('31-DEC-24','DD-MM-YY')-hire_date)/365.25)=25
 */
        System.out.println("Запросы SQL");
    }
}